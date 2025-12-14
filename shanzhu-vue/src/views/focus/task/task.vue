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
                <a-date-picker v-model:value="searchForm.planStartDate" placeholder="请选择计划开始日期时间" value-format="YYYY-MM-DD HH:mm:ss" show-time />
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="计划结束日期">
                <a-date-picker v-model:value="searchForm.planEndDate" placeholder="请选择计划结束日期时间" value-format="YYYY-MM-DD HH:mm:ss" show-time />
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

          <template v-else-if="column.key === 'goal'">
            <a-typography-text
              v-if="record.goal"
              :content="record.goal.title || '-'"
              :ellipsis="{ tooltip: record.goal.title }"
            />
            <span v-else>-</span>
          </template>

          <template v-else-if="column.key === 'status'">
            <div class="status-select-wrapper">
              <a-select
                :value="text"
                style="width: 120px"
                size="small"
                @change="(value: string) => handleStatusChange(record, value)"
                :bordered="false"
              >
                <a-select-option value="todo">
                  <div style="display: flex; align-items: center;">
                    <a-tag color="default" style="margin: 0; border-radius: 4px;">待办</a-tag>
                  </div>
                </a-select-option>
                <a-select-option value="in_progress">
                  <div style="display: flex; align-items: center;">
                    <a-tag color="processing" style="margin: 0; border-radius: 4px;">进行中</a-tag>
                  </div>
                </a-select-option>
                <a-select-option value="done">
                  <div style="display: flex; align-items: center;">
                    <a-tag color="success" style="margin: 0; border-radius: 4px;">已完成</a-tag>
                  </div>
                </a-select-option>
                <a-select-option value="cancelled">
                  <div style="display: flex; align-items: center;">
                    <a-tag color="error" style="margin: 0; border-radius: 4px;">已取消</a-tag>
                  </div>
                </a-select-option>
              </a-select>

              <!-- 自定义显示当前选中的状态 -->
              <div class="status-display" @click="$event.stopPropagation()">
                <a-tag
                  v-if="text === 'todo'"
                  color="default"
                  style="margin: 0; cursor: pointer;"
                >
                  待办
                </a-tag>
                <a-tag
                  v-else-if="text === 'in_progress'"
                  color="processing"
                  style="margin: 0; cursor: pointer;"
                >
                  进行中
                </a-tag>
                <a-tag
                  v-else-if="text === 'done'"
                  color="success"
                  style="margin: 0; cursor: pointer;"
                >
                  已完成
                </a-tag>
                <a-tag
                  v-else-if="text === 'cancelled'"
                  color="error"
                  style="margin: 0; cursor: pointer;"
                >
                  已取消
                </a-tag>
              </div>
            </div>
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

          <template v-else-if="column.key === 'planStartDate' || column.key === 'planEndDate' || column.key === 'actualStartDate' || column.key === 'actualEndDate'">
            {{ text ? dayjs(text).format('YYYY-MM-DD HH:mm:ss') : '-' }}
          </template>

          <template v-else-if="column.key === 'expectedDurationSec' || column.key === 'actualConsumedSec'">
            {{ formatDuration(text) }}
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

          <a-col :span="24">
            <a-form-item label="所属目标" name="goalId">
              <a-input-group compact>
                <a-input
                  :value="selectedGoalTitle"
                  placeholder="请选择所属目标"
                  readonly
                  style="width: calc(100% - 80px)"
                />
                <a-button type="primary" @click="handleOpenGoalModal" style="width: 80px">
                  <template #icon>
                    <PlusOutlined />
                  </template>
                  选择
                </a-button>
              </a-input-group>
              <div style="margin-top: 4px; color: #999; font-size: 12px">
                💡 提示：点击"选择"按钮选择目标（不包括已完成的目标）
              </div>
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <a-form-item label="任务标签" name="tagIds">
              <a-input-group compact>
                <a-select
                  v-model:value="selectedTagIds"
                  mode="multiple"
                  placeholder="请选择标签（可多选）"
                  :options="tagList.map(t => ({ value: t.id, label: t.name }))"
                  @change="handleTagChange"
                  allow-clear
                  style="width: calc(100% - 80px)"
                  :loading="tagLoading"
                  :show-search="true"
                  :filter-option="(input: string, option: any) => option.label.toLowerCase().includes(input.toLowerCase())"
                >
                  <template #notFoundContent>
                    <a-empty :image="Empty.PRESENTED_IMAGE_SIMPLE" description="暂无数据" />
                  </template>
                </a-select>
                <a-button type="primary" @click="handleTagAdd" style="width: 80px" :loading="tagLoading">
                  <template #icon>
                    <PlusOutlined />
                  </template>
                  新增
                </a-button>
              </a-input-group>
              <div style="margin-top: 8px">
                <a-input
                  v-model:value="tagSearchValue"
                  placeholder="输入新标签名称"
                  style="width: calc(100% - 88px); margin-right: 8px"
                  @pressEnter="handleTagAdd"
                />
              </div>
              <div style="margin-top: 4px; color: #999; font-size: 12px">
                💡 提示：可输入新标签名称后点击"新增"按钮快速创建，支持多选
              </div>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="权重" name="weight">
              <a-slider v-model:value="modalForm.weight" :min="0" :max="100" />
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="任务状态" name="status">
              <a-select v-model:value="modalForm.status" placeholder="请选择任务状态" :disabled="!isEdit">
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
              <a-date-picker v-model:value="modalForm.planStartDate" placeholder="请选择计划开始日期时间" value-format="YYYY-MM-DD HH:mm:ss" show-time style="width: 100%" />
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="计划结束日期" name="planEndDate">
              <a-date-picker v-model:value="modalForm.planEndDate" placeholder="请选择计划结束日期时间" value-format="YYYY-MM-DD HH:mm:ss" show-time style="width: 100%" />
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="进度百分比" name="progressRate">
              <a-slider v-model:value="modalForm.progressRate" :min="0" :max="100" />
            </a-form-item>
          </a-col>

          <!-- 实际开始时间：仅在进行中或已完成时显示 -->
          <a-col :span="12" v-if="modalForm.status === 'in_progress' || modalForm.status === 'done'">
            <a-form-item label="实际开始时间" name="actualStartDate">
              <a-date-picker
                v-model:value="modalForm.actualStartDate"
                placeholder="请选择实际开始时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                show-time
                style="width: 100%"
              />
            </a-form-item>
          </a-col>

          <!-- 实际结束时间：仅在已完成时显示 -->
          <a-col :span="12" v-if="modalForm.status === 'done'">
            <a-form-item label="实际结束时间" name="actualEndDate">
              <a-date-picker
                v-model:value="modalForm.actualEndDate"
                placeholder="请选择实际结束时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                show-time
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <template #footer>
        <a-button @click="handleModalCancel">关 闭</a-button>
        <a-button type="primary" @click="handleModalOk" :loading="modalConfirmLoading">保 存</a-button>
      </template>
    </a-modal>

    <!-- 目标选择弹窗 -->
    <a-modal
      v-model:open="goalModalVisible"
      title="选择目标"
      width="800px"
      :footer="null"
    >
      <a-table
        :columns="[
          { title: '目标标题', dataIndex: 'title', key: 'title' },
          { title: '状态', dataIndex: 'status', key: 'status', width: 100 },
          { title: '开始日期', dataIndex: 'startDate', key: 'startDate', width: 180 },
          { title: '结束日期', dataIndex: 'endDate', key: 'endDate', width: 180 },
          { title: '操作', key: 'action', width: 100 }
        ]"
        :data-source="goalList.filter((goal: FocusGoal) => goal.status === 'active')"
        :loading="goalLoading"
        :pagination="{ pageSize: 5 }"
        row-key="id"
      >
        <template #bodyCell="{ column, record, text }">
          <template v-if="column.key === 'status'">
            <a-tag v-if="text === 'draft'" color="default">草稿</a-tag>
            <a-tag v-else-if="text === 'active'" color="processing">进行中</a-tag>
            <a-tag v-else-if="text === 'archived'" color="warning">已归档</a-tag>
            <span v-else>{{ text }}</span>
          </template>
          <template v-else-if="column.key === 'startDate' || column.key === 'endDate'">
            {{ text ? dayjs(text).format('YYYY-MM-DD HH:mm:ss') : '' }}
          </template>
          <template v-else-if="column.key === 'action'">
            <a-button type="link" size="small" @click="() => handleSelectGoal(record)">
              选择
            </a-button>
          </template>
        </template>
      </a-table>
    </a-modal>

    <!-- 状态时间输入弹窗 -->
    <a-modal
      v-model:open="statusTimeModalVisible"
      :title="statusTimeModalTitle"
      :confirm-loading="statusTimeModalLoading"
      @ok="handleStatusTimeModalOk"
      @cancel="handleStatusTimeModalCancel"
      width="400px"
    >
      <a-form layout="vertical">
        <a-form-item :label="statusTimeModalTitle">
          <a-date-picker
            v-model:value="statusTimeValue"
            :placeholder="'请选择' + statusTimeModalTitle"
            value-format="YYYY-MM-DD HH:mm:ss"
            show-time
            style="width: 100%"
          />
        </a-form-item>
        <a-alert
          v-if="statusTimeModalTitle.includes('开始')"
          message="任务将变更为「进行中」状态"
          type="info"
          show-icon
          style="margin-top: 8px"
        />
        <a-alert
          v-else
          message="任务将变更为「已完成」状态"
          type="success"
          show-icon
          style="margin-top: 8px"
        />
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onActivated } from 'vue'
import { message, Modal, Empty } from 'ant-design-vue'
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
import { listFocusTag, saveFocusTag } from '@/api/focus/tag'
import type { FocusTag } from '@/api/focus/tag/types'
import { listFocusGoal } from '@/api/focus/goal'
import type { FocusGoal } from '@/api/focus/goal/types'
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
    title: '所属目标',
    dataIndex: 'goal',
    key: 'goal',
    width: 200
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
    width: 180
  },
  {
    title: '计划结束日期',
    dataIndex: 'planEndDate',
    key: 'planEndDate',
    width: 180
  },
  {
    title: '实际开始时间',
    dataIndex: 'actualStartDate',
    key: 'actualStartDate',
    width: 180
  },
  {
    title: '实际结束时间',
    dataIndex: 'actualEndDate',
    key: 'actualEndDate',
    width: 180
  },
  {
    title: '预期时长',
    dataIndex: 'expectedDurationSec',
    key: 'expectedDurationSec',
    width: 120
  },
  {
    title: '实际消耗',
    dataIndex: 'actualConsumedSec',
    key: 'actualConsumedSec',
    width: 120
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

// ========== 标签相关状态 ==========
const tagList = ref<FocusTag[]>([])
const tagLoading = ref<boolean>(false)
const selectedTagIds = ref<number[]>([]) // 已选中的标签ID列表
const tagSearchValue = ref<string>('') // 标签搜索值

// ========== 目标相关状态 ==========
const goalList = ref<FocusGoal[]>([])
const goalLoading = ref<boolean>(false)
const goalModalVisible = ref<boolean>(false) // 目标选择弹窗
const selectedGoalId = ref<number | undefined>(undefined)
const selectedGoalTitle = ref<string>('')

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
  actualStartDate: undefined,
  actualEndDate: undefined,
  progressRate: 0
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
    id: undefined,
    title: '',
    goalId: undefined,
    weight: 100,
    status: 'todo',
    priority: 'medium',
    planStartDate: undefined,
    planEndDate: undefined,
    actualStartDate: undefined,
    actualEndDate: undefined,
    progressRate: 0
  })
  selectedTagIds.value = [] // 清空标签选择
  tagSearchValue.value = '' // 清空标签搜索值
  selectedGoalId.value = undefined // 清空目标选择
  selectedGoalTitle.value = '' // 清空目标标题
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

    // 设置已选中的标签
    if (response.data.tagIds && response.data.tagIds.length > 0) {
      selectedTagIds.value = response.data.tagIds.map((id: string) => Number(id))
    } else {
      selectedTagIds.value = []
    }

    // 设置目标信息
    if (response.data.goalId) {
      selectedGoalId.value = response.data.goalId

      // 优先使用后端返回的 goal 对象（如果存在）
      if (response.data.goal && response.data.goal.title) {
        selectedGoalTitle.value = response.data.goal.title
      } else {
        // 否则从 goalList 中查找
        const goal = goalList.value.find(g => g.id === response.data.goalId)
        selectedGoalTitle.value = goal ? goal.title || '' : `目标ID: ${response.data.goalId}`
      }
    } else {
      selectedGoalId.value = undefined
      selectedGoalTitle.value = ''
    }

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
          formData.planStartDate = dayjs(formData.planStartDate).format('YYYY-MM-DD HH:mm:ss')
        }
        if (formData.planEndDate) {
          formData.planEndDate = dayjs(formData.planEndDate).format('YYYY-MM-DD HH:mm:ss')
        }

        // 添加标签ID（转换为字符串数组）
        formData.tagIds = selectedTagIds.value.map(id => String(id))

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

// 获取标签列表
const fetchTagList = async () => {
  try {
    tagLoading.value = true
    const response = await listFocusTag({})
    tagList.value = response.data || []
  } catch (err) {
    console.error('获取标签列表失败:', err)
  } finally {
    tagLoading.value = false
  }
}

// 获取目标列表（获取所有状态的目标，用于查找和回显）
const fetchGoalList = async () => {
  try {
    goalLoading.value = true
    const response = await listFocusGoal({})
    // 获取所有状态的目标（用于查找和回显）
    goalList.value = response.data || []
  } catch (err) {
    console.error('获取目标列表失败:', err)
  } finally {
    goalLoading.value = false
  }
}

// 标签选择变化
const handleTagChange = (values: number[]) => {
  selectedTagIds.value = values
}

// 快速新增标签
const handleTagAdd = async () => {
  if (!tagSearchValue.value || tagSearchValue.value.trim() === '') {
    message.warning('请输入标签名称')
    return
  }

  // 检查是否已存在
  const existTag = tagList.value.find(
    t => t.name === tagSearchValue.value.trim()
  )
  if (existTag) {
    // 如果已存在且未选中，则自动选中
    if (!selectedTagIds.value.includes(existTag.id!)) {
      selectedTagIds.value.push(existTag.id!)
    }
    tagSearchValue.value = ''
    message.info('该标签已存在，已自动选择')
    return
  }

  try {
    tagLoading.value = true
    const newTag: FocusTag = {
      name: tagSearchValue.value.trim(),
      color: '#1890ff'
    }

    const response = await saveFocusTag(newTag)

    // 检查响应状态码
    if (response.code === 200) {
      message.success('标签创建成功')

      // 重新加载标签列表
      await fetchTagList()

      // 自动选择新创建的标签
      const created = tagList.value.find(t => t.name === newTag.name)
      if (created && !selectedTagIds.value.includes(created.id!)) {
        selectedTagIds.value.push(created.id!)
      }

      // 清空搜索值
      tagSearchValue.value = ''
    } else {
      message.error(response.msg || '创建标签失败')
    }
  } catch (err) {
    console.error('创建标签失败:', err)
    message.error('创建标签失败')
  } finally {
    tagLoading.value = false
  }
}

// 打开目标选择弹窗
const handleOpenGoalModal = () => {
  goalModalVisible.value = true
}

// 选择目标
const handleSelectGoal = (goal: FocusGoal) => {
  selectedGoalId.value = goal.id
  selectedGoalTitle.value = goal.title || ''
  modalForm.goalId = goal.id
  goalModalVisible.value = false
}

// 时间格式化函数：将秒转换为可读格式
const formatDuration = (seconds?: number): string => {
  if (!seconds || seconds === 0) {
    return '-'
  }

  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  const secs = seconds % 60

  const parts = []
  if (hours > 0) {
    parts.push(`${hours}小时`)
  }
  if (minutes > 0) {
    parts.push(`${minutes}分钟`)
  }
  if (secs > 0 && hours === 0) { // 只有在小于1小时时才显示秒
    parts.push(`${secs}秒`)
  }

  return parts.length > 0 ? parts.join('') : '-'
}

// ========== 状态时间输入弹窗相关 ==========
const statusTimeModalVisible = ref<boolean>(false)
const statusTimeModalLoading = ref<boolean>(false)
const statusTimeModalTitle = ref<string>('')
const statusTimeValue = ref<string>()
const pendingStatusChange = ref<{ record: FocusTask, newStatus: string } | null>(null)

// 快速修改任务状态
const handleStatusChange = async (record: FocusTask, newStatus: string) => {
  // 如果状态变为进行中，需要输入实际开始时间
  if (newStatus === 'in_progress') {
    statusTimeModalTitle.value = '请输入实际开始时间'
    statusTimeValue.value = undefined
    pendingStatusChange.value = { record, newStatus }
    statusTimeModalVisible.value = true
    return
  }

  // 如果状态变为已完成，需要输入实际结束时间
  if (newStatus === 'done') {
    statusTimeModalTitle.value = '请输入实际结束时间'
    statusTimeValue.value = undefined
    pendingStatusChange.value = { record, newStatus }
    statusTimeModalVisible.value = true
    return
  }

  // 其他状态直接更新
  await updateTaskStatus(record, newStatus)
}

// 实际更新任务状态的方法
const updateTaskStatus = async (record: FocusTask, newStatus: string, timeValue?: string) => {
  try {
    const updateData: FocusTask = {
      id: record.id,
      status: newStatus,
      title: record.title,
      goalId: record.goalId,
      weight: record.weight,
      priority: record.priority,
      planStartDate: record.planStartDate,
      planEndDate: record.planEndDate,
      actualStartDate: record.actualStartDate,
      actualEndDate: record.actualEndDate,
      progressRate: record.progressRate
    }

    // 如果是进行中，设置实际开始时间
    if (newStatus === 'in_progress' && timeValue) {
      updateData.actualStartDate = timeValue
    }

    // 如果是已完成，设置实际结束时间
    if (newStatus === 'done' && timeValue) {
      updateData.actualEndDate = timeValue
    }

    const result = await saveFocusTask(updateData)
    if (result.code === 200 && result.data) {
      message.success('状态更新成功')
      // 刷新数据以获取最新的统计信息
      fetchData()
    } else {
      message.error(result.msg || '状态更新失败')
      fetchData()
    }
  } catch (err) {
    console.error('更新任务状态失败:', err)
    message.error('状态更新失败')
    fetchData()
  }
}

// 确认时间输入
const handleStatusTimeModalOk = async () => {
  if (!statusTimeValue.value) {
    message.warning('请选择时间')
    return
  }

  if (!pendingStatusChange.value) {
    return
  }

  statusTimeModalLoading.value = true
  try {
    await updateTaskStatus(
      pendingStatusChange.value.record,
      pendingStatusChange.value.newStatus,
      statusTimeValue.value
    )
    statusTimeModalVisible.value = false
  } finally {
    statusTimeModalLoading.value = false
  }
}

// 取消时间输入
const handleStatusTimeModalCancel = () => {
  statusTimeModalVisible.value = false
  pendingStatusChange.value = null
  statusTimeValue.value = undefined
}

// 初始化数据
onMounted(() => {
  fetchData()
  fetchTagList() // 加载标签列表
  fetchGoalList() // 加载目标列表
})

// 解决 keep-alive 缓存问题，每次组件激活时重新查询数据
onActivated(() => {
  console.log('🔄 任务页面被激活，重新查询数据...')
  fetchData() // 重新查询任务数据
  fetchTagList() // 重新查询标签列表，避免缓存
  fetchGoalList() // 重新查询目标列表，避免缓存
})
</script>

<style scoped lang="less">
.default-input-width {
  width: 160px;
}

.status-select-wrapper {
  position: relative;
  width: 120px;

  .ant-select {
    opacity: 0;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 2;
  }

  .status-display {
    position: relative;
    z-index: 1;
    pointer-events: none;
    display: flex;
    align-items: center;
    height: 24px;

    .ant-tag {
      user-select: none;
    }
  }

  &:hover {
    .status-display .ant-tag {
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      transform: translateY(-1px);
      transition: all 0.2s ease;
    }
  }
}
</style>