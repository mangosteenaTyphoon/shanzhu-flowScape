<template>
  <div>
    <a-flex :gap="16" vertical>
      <!-- 搜索区域 -->
      <a-card :style="{ border: 'none' }" :body-style="{ 'padding-bottom': '0' }">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="任务标题">
                <a-input v-model:value="searchForm.title" placeholder="请输入任务标题" allow-clear />
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="任务状态">
                <a-select v-model:value="searchForm.status" placeholder="请选择任务状态" allow-clear style="width: 120px">
                  <a-select-option value="todo">待办</a-select-option>
                  <a-select-option value="in_progress">进行中</a-select-option>
                  <a-select-option value="done">已完成</a-select-option>
                  <a-select-option value="cancelled">已取消</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="计划开始日期">
                <a-date-picker v-model:value="searchForm.planStartDate" placeholder="请选择计划开始日期" value-format="YYYY-MM-DD" />
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="计划结束日期">
                <a-date-picker v-model:value="searchForm.planEndDate" placeholder="请选择计划结束日期" value-format="YYYY-MM-DD" />
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item>
                <a-space size="small">
                  <a-button type="primary" @click="handleSearch" :loading="loading">
                    <template #icon>
                      <SearchOutlined />
                    </template>
                    查 询
                  </a-button>
                  <a-button @click="resetSearch" :loading="loading">
                    <template #icon>
                      <RedoOutlined />
                    </template>
                    重 置
                  </a-button>
                </a-space>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-card>

      <!-- 数据表格区域 -->
      <a-table
        :columns="columns"
        :data-source="dataSource"
        :pagination="false"
        :loading="loading"
        :row-selection="rowSelection"
        row-key="id"
        :scroll="{ x: 1200 }"
      >
        <template #title>
          <a-flex :gap="8" wrap="wrap">
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <PlusOutlined />
              </template>
              新 增
            </a-button>
            <a-button type="primary" danger @click="handleBatchDelete" :disabled="selectedRowKeys.length === 0">
              <template #icon>
                <DeleteOutlined />
              </template>
              删 除
              <span v-if="selectedRowKeys && selectedRowKeys.length > 0" style="margin-left: 4px"> {{ selectedRowKeys.length }} 项</span>
            </a-button>
            
            <!-- 表格设置 -->
            <table-setting v-model="columns" />
          </a-flex>
        </template>
        
        <template #bodyCell="{ column, record, text }">
          <template v-if="column.key === 'title'">
            <a-typography-text :content="text" :ellipsis="{ tooltip: text }" />
          </template>
          
          <template v-else-if="column.key === 'status'">
            <a-tag v-if="text === 'todo'" color="default">待办</a-tag>
            <a-tag v-else-if="text === 'in_progress'" color="processing">进行中</a-tag>
            <a-tag v-else-if="text === 'done'" color="success">已完成</a-tag>
            <a-tag v-else-if="text === 'cancelled'" color="error">已取消</a-tag>
            <span v-else>{{ text }}</span>
          </template>
          
          <template v-else-if="column.key === 'priority'">
            <a-tag v-if="text === 'high'" color="error">高</a-tag>
            <a-tag v-else-if="text === 'medium'" color="warning">中</a-tag>
            <a-tag v-else-if="text === 'low'" color="processing">低</a-tag>
            <span v-else>{{ text }}</span>
          </template>
          
          <template v-else-if="column.key === 'progressRate'">
            <a-progress :percent="text" size="small" />
          </template>
          
          <template v-else-if="column.key === 'planStartDate' || column.key === 'planEndDate'">
            {{ text ? dayjs(text).format('YYYY-MM-DD') : '-' }}
          </template>
          
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="link" size="small" @click="handleEdit(record)">
                <template #icon>
                  <EditOutlined />
                </template>
                编辑
              </a-button>
              <a-divider type="vertical" />
              <a-button type="link" danger size="small" @click="handleDelete([record.id])">
                <template #icon>
                  <DeleteOutlined />
                </template>
                删除
              </a-button>
            </a-space>
          </template>
        </template>
        
        <template #footer>
          <a-flex justify="flex-end">
            <a-pagination
              v-model:current="pagination.current"
              v-model:page-size="pagination.pageSize"
              show-size-changer
              :total="pagination.total"
              :show-total="(total: number) => `共 ${total} 条`"
              @change="handlePageChange"
            />
          </a-flex>
        </template>
      </a-table>
    </a-flex>

    <!-- 编辑/新增模态框 -->
    <a-modal
      v-model:open="modalVisible"
      :confirm-loading="modalConfirmLoading"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
      :width="600"
    >
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{ modalTitle }}</a-typography-title>
        </div>
      </template>
      
      <a-form
        layout="vertical"
        :model="modalForm"
        :rules="modalRules"
        ref="modalFormRef"
      >
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="任务标题" name="title">
              <a-input v-model:value="modalForm.title" placeholder="请输入任务标题" />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="所属目标ID" name="goalId">
              <a-input-number v-model:value="modalForm.goalId" placeholder="请输入所属目标ID" style="width: 100%" />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="权重" name="weight">
              <a-slider v-model:value="modalForm.weight" :min="0" :max="100" />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="任务状态" name="status">
              <a-select v-model:value="modalForm.status" placeholder="请选择任务状态">
                <a-select-option value="todo">待办</a-select-option>
                <a-select-option value="in_progress">进行中</a-select-option>
                <a-select-option value="done">已完成</a-select-option>
                <a-select-option value="cancelled">已取消</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="优先级" name="priority">
              <a-select v-model:value="modalForm.priority" placeholder="请选择优先级">
                <a-select-option value="high">高</a-select-option>
                <a-select-option value="medium">中</a-select-option>
                <a-select-option value="low">低</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="计划开始日期" name="planStartDate">
              <a-date-picker v-model:value="modalForm.planStartDate" placeholder="请选择计划开始日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="计划结束日期" name="planEndDate">
              <a-date-picker v-model:value="modalForm.planEndDate" placeholder="请选择计划结束日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="进度百分比" name="progressRate">
              <a-slider v-model:value="modalForm.progressRate" :min="0" :max="100" />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="预期持续时间(秒)" name="expectedDurationSec">
              <a-input-number v-model:value="modalForm.expectedDurationSec" placeholder="请输入预期持续时间" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      
      <template #footer>
        <a-button @click="handleModalCancel">关 闭</a-button>
        <a-button type="primary" @click="handleModalOk" :loading="modalConfirmLoading">保 存</a-button>
      </template>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { 
  SearchOutlined, 
  RedoOutlined, 
  PlusOutlined, 
  DeleteOutlined,
  EditOutlined
} from '@ant-design/icons-vue'
import dayjs from 'dayjs'
import type { TableProps, FormInstance } from 'ant-design-vue'
import { FocusTask, FocusTaskQueryParams } from '@/api/focus/task/types'
import { getFocusTaskPage, getFocusTask, saveFocusTask, deleteFocusTask } from '@/api/focus/task'
import TableSetting from '@/components/table-setting/index.vue'

// 搜索表单
const searchForm = reactive<FocusTaskQueryParams>({
  pageNum: 1,
  pageSize: 10,
  title: undefined,
  status: undefined,
  planStartDate: undefined,
  planEndDate: undefined
})

// 表格相关
const dataSource = ref<FocusTask[]>([])
const loading = ref<boolean>(false)
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  pageSizeOptions: ['10', '20', '50', '100']
})

// 表格列定义
const columns = ref([
  {
    title: '任务标题',
    dataIndex: 'title',
    key: 'title',
    fixed: 'left',
    width: 200
  },
  {
    title: '所属目标ID',
    dataIndex: 'goalId',
    key: 'goalId',
    width: 120
  },
  {
    title: '任务状态',
    dataIndex: 'status',
    key: 'status',
    width: 120
  },
  {
    title: '优先级',
    dataIndex: 'priority',
    key: 'priority',
    width: 100
  },
  {
    title: '进度',
    dataIndex: 'progressRate',
    key: 'progressRate',
    width: 150
  },
  {
    title: '权重',
    dataIndex: 'weight',
    key: 'weight',
    width: 100
  },
  {
    title: '计划开始日期',
    dataIndex: 'planStartDate',
    key: 'planStartDate',
    width: 130
  },
  {
    title: '计划结束日期',
    dataIndex: 'planEndDate',
    key: 'planEndDate',
    width: 130
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 150
  }
])

// 表格行选择
const selectedRowKeys = ref<number[]>([])
const rowSelection: TableProps['rowSelection'] = {
  selectedRowKeys: selectedRowKeys.value,
  onChange: (selectedKeys: number[]) => {
    selectedRowKeys.value = selectedKeys
  }
}

// 搜索表单引用
const searchFormRef = ref<FormInstance>()

// 模态框相关
const modalVisible = ref<boolean>(false)
const modalConfirmLoading = ref<boolean>(false)
const modalTitle = ref<string>('')
const isEdit = ref<boolean>(false)
const modalFormRef = ref()
const modalForm = reactive<FocusTask>({
  title: '',
  goalId: undefined,
  weight: 100,
  status: 'todo',
  priority: 'medium',
  planStartDate: undefined,
  planEndDate: undefined,
  progressRate: 0,
  expectedDurationSec: undefined
})

// 表单验证规则
const modalRules = {
  title: [{ required: true, message: '请输入任务标题', trigger: 'blur' }],
  goalId: [{ required: true, message: '请输入所属目标ID', trigger: 'change' }],
  planStartDate: [{ required: true, message: '请选择计划开始日期', trigger: 'change' }],
  planEndDate: [{ required: true, message: '请选择计划结束日期', trigger: 'change' }]
}

// 获取数据
const fetchData = async () => {
  try {
    loading.value = true
    const response = await getFocusTaskPage(searchForm)
    dataSource.value = response.data.records
    pagination.total = response.data.total
    pagination.current = response.data.current
    pagination.pageSize = response.data.size
  } catch (err) {
    console.error('获取专注任务列表失败:', err)
    message.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  searchForm.pageNum = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    status: undefined,
    planStartDate: undefined,
    planEndDate: undefined
  })
  fetchData()
}

// 表格分页变化
const handlePageChange = (page: number, pageSize: number) => {
  searchForm.pageNum = page
  searchForm.pageSize = pageSize
  fetchData()
}

// 新增操作
const handleAdd = () => {
  modalTitle.value = '新增专注任务'
  isEdit.value = false
  Object.assign(modalForm, {
    title: '',
    goalId: undefined,
    weight: 100,
    status: 'todo',
    priority: 'medium',
    planStartDate: undefined,
    planEndDate: undefined,
    progressRate: 0,
    expectedDurationSec: undefined
  })
  modalVisible.value = true
}

// 编辑操作
const handleEdit = async (record: FocusTask) => {
  modalTitle.value = '编辑专注任务'
  isEdit.value = true
  modalVisible.value = true
  
  try {
    const response = await getFocusTask(record.id!)
    Object.assign(modalForm, response.data)
    // 处理日期格式
    if (response.data.planStartDate) {
      modalForm.planStartDate = dayjs(response.data.planStartDate) as unknown as string
    }
    if (response.data.planEndDate) {
      modalForm.planEndDate = dayjs(response.data.planEndDate) as unknown as string
    }
  } catch (err) {
    console.error('获取专注任务详情失败:', err)
    message.error('获取详情失败')
  }
}

// 模态框确认
const handleModalOk = () => {
  modalFormRef.value
    .validate()
    .then(async () => {
      modalConfirmLoading.value = true
      try {
        const formData = { ...modalForm }
        // 处理日期格式
        if (formData.planStartDate) {
          formData.planStartDate = dayjs(formData.planStartDate).format('YYYY-MM-DD')
        }
        if (formData.planEndDate) {
          formData.planEndDate = dayjs(formData.planEndDate).format('YYYY-MM-DD')
        }
        
        const result = await saveFocusTask(formData)
        if (result.code === 200 && result.data) {
          message.success(`${isEdit.value ? '编辑' : '新增'}成功`)
          modalVisible.value = false
          fetchData()
        } else {
          message.error(result.msg || `${isEdit.value ? '编辑' : '新增'}失败`)
        }
      } catch (err) {
        console.error('保存专注任务失败:', err)
        message.error('保存失败')
      } finally {
        modalConfirmLoading.value = false
      }
    })
    .catch(() => {
      // 表单验证失败
    })
}

// 模态框取消
const handleModalCancel = () => {
  modalVisible.value = false
  modalFormRef.value.resetFields()
}

// 删除操作
const handleDelete = (ids: number[]) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除选中的${ids.length}条记录吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        const result = await deleteFocusTask(ids)
        if (result.code === 200 && result.data) {
          message.success('删除成功')
          // 清除选中项
          selectedRowKeys.value = []
          fetchData()
        } else {
          message.error(result.msg || '删除失败')
        }
      } catch (err) {
        console.error('删除专注任务失败:', err)
        message.error('删除失败')
      }
    }
  })
}

// 批量删除
const handleBatchDelete = () => {
  if (selectedRowKeys.value.length === 0) {
    message.warning('请至少选择一条记录')
    return
  }
  handleDelete(selectedRowKeys.value)
}

// 初始化数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="less">
.default-input-width {
  width: 160px;
}
</style>