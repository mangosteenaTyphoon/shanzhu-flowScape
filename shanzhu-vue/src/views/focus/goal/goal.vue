<template>
  <div>
    <a-flex :gap="16" vertical>
      <!-- 搜索条件 -->
      <a-card :style="{border: 'none'}" :body-style="{'padding-bottom': '0'}">
        <a-form :colon="false">
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="目标标题">
                <a-input v-model:value="searchForm.title" placeholder="请输入目标标题" allow-clear />
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="目标状态">
                <a-select v-model:value="searchForm.status" placeholder="请选择目标状态" allow-clear style="width: 120px">
                  <a-select-option value="draft">草稿</a-select-option>
                  <a-select-option value="active">进行中</a-select-option>
                  <a-select-option value="completed">已完成</a-select-option>
                  <a-select-option value="archived">已归档</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="开始日期">
                <a-date-picker v-model:value="searchForm.startDate" placeholder="请选择开始日期时间" value-format="YYYY-MM-DD HH:mm:ss" show-time />
              </a-form-item>
            </a-col>
            <a-col>
              <a-form-item label="结束日期">
                <a-date-picker v-model:value="searchForm.endDate" placeholder="请选择结束日期时间" value-format="YYYY-MM-DD HH:mm:ss" show-time />
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

      <!-- 数据表格 -->
      <a-table
        :columns="columns"
        :data-source="dataSource"
        :loading="loading"
        :pagination="false"
        :row-selection="rowSelection"
        row-key="id"
        :scroll="{x: 'max-content'}"
      >
        <template #title>
          <a-flex :gap="8" wrap="wrap">
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <PlusOutlined />
              </template>
              新 增
            </a-button>
            <a-popconfirm title="删除后不可恢复，是否删除？"
                          :open="openDeletePopconfirm"
                          ok-text="确 定"
                          cancel-text="取 消"
                          @confirm="handleBatchDelete"
                          @cancel="closePopconfirm"
                          @open-change="(open: boolean) => !open ? closePopconfirm(): ''"
            >
              <a-button danger @click="openPopconfirm">
                <template #icon>
                  <DeleteOutlined />
                </template>
                删 除
                <span v-if="selectedRowKeys && selectedRowKeys.length > 0" style="margin-left: 4px"> {{selectedRowKeys.length}} 项</span>
              </a-button>
            </a-popconfirm>

            <!-- 表格设置 -->
            <table-setting v-model="columns"/>
          </a-flex>
        </template>
        <template #bodyCell="{ column, record, text }">
          <template v-if="column.key === 'title'">
            <a-tooltip placement="topLeft" :title="record.description || '暂无描述'">
              <a-typography-text ellipsis>{{ text }}</a-typography-text>
            </a-tooltip>
          </template>
          <template v-else-if="column.key === 'status'">
            <a-tag v-if="text === 'draft'" color="default">草稿</a-tag>
            <a-tag v-else-if="text === 'active'" color="processing">进行中</a-tag>
            <a-tag v-else-if="text === 'completed'" color="success">已完成</a-tag>
            <a-tag v-else-if="text === 'archived'" color="warning">已归档</a-tag>
            <span v-else>{{ text }}</span>
          </template>
          <template v-else-if="column.key === 'startDate' || column.key === 'endDate'">
            {{ text ? dayjs(text).format('YYYY-MM-DD HH:mm:ss') : '' }}
          </template>
          <template v-else-if="column.key === 'finalProgress'">
            <a-progress :percent="text" :show-info="false" />
          </template>
          <template v-else-if="column.key === 'hasDelayedTasks'">
            <a-tag v-if="text" color="error">有延期</a-tag>
            <a-tag v-else color="success">无延期</a-tag>
          </template>
          <template v-else-if="column.key === 'expectedDurationSec'">
            {{ formatDuration(text) }}
          </template>
          <template v-else-if="column.key === 'actualDurationSec'">
            {{ formatDuration(text) }}
          </template>
          <template v-else-if="column.key === 'overdueCompletionTimeSec'">
            <span v-if="text && text > 0" style="color: #ff4d4f">{{ formatDuration(text) }}</span>
            <span v-else style="color: #52c41a">-</span>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="link" size="small" @click="(event: MouseEvent) => handleDetailClick(event, record)">
                <template #icon>
                  <EyeOutlined />
                </template>
                详情
              </a-button>
              <a-divider type="vertical"/>
              <a-button type="link" size="small" @click="(event: MouseEvent) => handleEditClick(event, record)">
                <template #icon>
                  <EditOutlined />
                </template>
                编辑
              </a-button>
              <a-divider type="vertical"/>
              <a-popconfirm title="删除后不可恢复，是否删除？"
                            placement="bottomRight"
                            ok-text="确 定"
                            cancel-text="取 消"
                            @confirm="() => handleDelete(record.id)"
              >
                <a-button type="link" danger size="small" @click="(event: MouseEvent) => event.stopPropagation()">
                  <template #icon>
                    <DeleteOutlined />
                  </template>
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
        <template #footer>
          <a-flex justify="flex-end">
            <a-pagination v-model:current="pagination.current"
                          v-model:page-size="pagination.pageSize"
                          show-size-changer
                          :total="pagination.total"
                          :show-total="(total:number) => `共 ${total} 条`"
                          @change="handlePageChange"
            />
          </a-flex>
        </template>
      </a-table>
    </a-flex>

    <!-- 编辑/新增对话框 -->
    <a-modal
      v-model:open="modalVisible"
      :confirm-loading="modalConfirmLoading"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
      width="600px"
    >
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{ modalTitle }}</a-typography-title>
        </div>
      </template>

      <a-form
        ref="modalFormRef"
        :model="modalForm"
        :rules="modalRules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        :colon="false"
      >
        <a-form-item label="目标标题" name="title">
          <a-input v-model:value="modalForm.title" placeholder="请输入目标标题" />
        </a-form-item>
        <a-form-item label="目标描述" name="description">
          <a-textarea v-model:value="modalForm.description" placeholder="请输入目标描述" :rows="3" />
        </a-form-item>
        <a-form-item label="目标分类" name="categoryId">
          <a-input-group compact>
            <a-auto-complete
              v-model:value="categorySearchValue"
              :options="filteredCategoryOptions"
              placeholder="请选择或输入分类"
              :filter-option="filterCategoryOption"
              @select="handleCategorySelect"
              allow-clear
              style="width: calc(100% - 80px)"
            >
              <template #notFoundContent>
                <a-empty :image="Empty.PRESENTED_IMAGE_SIMPLE" description="暂无数据">
                  <template #description>
                    <span style="color: #999">输入分类名称后点击"新增"按钮</span>
                  </template>
                </a-empty>
              </template>
            </a-auto-complete>
            <a-button type="primary" @click="handleCategoryAdd" style="width: 80px" :loading="categoryLoading">
              <template #icon>
                <PlusOutlined />
              </template>
              新增
            </a-button>
          </a-input-group>
          <div style="margin-top: 4px; color: #999; font-size: 12px">
            💡 提示：可直接输入新分类名称，点击"新增"按钮快速创建
          </div>
        </a-form-item>
        <a-form-item label="目标标签" name="tagIds">
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
            <a-input
              v-model:value="tagSearchValue"
              placeholder="新标签名"
              style="width: calc(100% - 160px); display: none"
            />
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
        <a-row>
          <a-col :span="12">
            <a-form-item label="开始日期" :label-col="{span: 8}" name="startDate">
              <a-date-picker v-model:value="modalForm.startDate" placeholder="请选择开始日期时间" value-format="YYYY-MM-DD HH:mm:ss" show-time style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="结束日期" :label-col="{span: 8}" name="endDate">
              <a-date-picker v-model:value="modalForm.endDate" placeholder="请选择结束日期时间" value-format="YYYY-MM-DD HH:mm:ss" show-time style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
        <!-- 状态不可选择，通过按钮控制 -->
        <!-- 进度由子任务自动计算，不可手动编辑 -->
        <a-form-item label="目标进度">
          <a-progress :percent="modalForm.finalProgress || 0" />
          <div style="margin-top: 4px; color: #999; font-size: 12px">
            💡 提示：进度由关联的子任务自动计算，无子任务时进度为 0%
          </div>
        </a-form-item>
        <!-- 统计信息展示（只读） -->
        <a-form-item label="延期状态">
          <a-tag v-if="modalForm.hasDelayedTasks" color="error">有延期任务</a-tag>
          <a-tag v-else color="success">无延期任务</a-tag>
        </a-form-item>
        <a-row>
          <a-col :span="12">
            <a-form-item label="预期时长" :label-col="{span: 8}">
              <span>{{ formatDuration(modalForm.expectedDurationSec) }}</span>
              <div style="margin-top: 4px; color: #999; font-size: 12px">
                💡 所有子任务预期时长之和
              </div>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="实际时长" :label-col="{span: 8}">
              <span>{{ formatDuration(modalForm.actualDurationSec) }}</span>
              <div style="margin-top: 4px; color: #999; font-size: 12px">
                💡 所有子任务实际时长之和
              </div>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="超期时长">
          <span v-if="modalForm.overdueCompletionTimeSec && modalForm.overdueCompletionTimeSec > 0"
                style="color: #ff4d4f; font-weight: bold">
            {{ formatDuration(modalForm.overdueCompletionTimeSec) }}
          </span>
          <span v-else style="color: #52c41a">无超期</span>
          <div style="margin-top: 4px; color: #999; font-size: 12px">
            💡 实际时长 - 预期时长
          </div>
        </a-form-item>
      </a-form>

      <template #footer>
        <a-button @click="handleModalCancel">关 闭</a-button>
        <!-- 新增和编辑都显示两个按钮，通过按钮控制状态 -->
        <!-- 只有新增或草稿状态时才显示保存至草稿按钮 -->
        <a-button v-if="canSaveAsDraft" @click="handleSaveAsDraft" :loading="modalConfirmLoading">
          保存至草稿
        </a-button>
        <a-button type="primary" @click="handleSaveAsActive" :loading="modalConfirmLoading">
          开始目标
        </a-button>
      </template>
    </a-modal>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onActivated, computed } from 'vue'
import { useRouter } from 'vue-router'
import { message, Modal, Empty } from 'ant-design-vue'
import dayjs from 'dayjs'
import type { TableProps } from 'ant-design-vue'
import type { Dayjs } from 'dayjs'
import TableSetting from "@/components/table-setting/index.vue";

const router = useRouter()

// 图标引入
import {
  SearchOutlined,
  RedoOutlined,
  PlusOutlined,
  DeleteOutlined,
  EditOutlined,
  EyeOutlined
} from '@ant-design/icons-vue';

// API引入
import {
  pageFocusGoal,
  listFocusGoal,
  getFocusGoal,
  saveFocusGoal,
  deleteFocusGoal
} from '@/api/focus/goal'

// 添加分类相关的API引入
import {
  listFocusCategory,
  saveFocusCategory
} from '@/api/focus/category'
import type { FocusCategory } from '@/api/focus/category/types'

// 添加标签相关的API引入
import {
  listFocusTag,
  saveFocusTag
} from '@/api/focus/tag'
import type { FocusTag } from '@/api/focus/tag/types'



// 数据接口定义
interface FocusGoal {
  id?: number
  userId?: number
  title?: string
  description?: string
  categoryId?: number
  startDate?: string
  endDate?: string
  status?: string
  finalProgress?: number
  completionStatus?: string
  hasDelayedTasks?: boolean
  expectedDurationSec?: number
  actualDurationSec?: number
  overdueCompletionTimeSec?: number
  createdAt?: string
  updatedAt?: string
}

// 搜索表单
const searchForm = reactive<FocusGoal>({
  title: undefined,
  status: undefined,
  startDate: undefined,
  endDate: undefined
})

// 表格相关
const dataSource = ref<FocusGoal[]>([])
const loading = ref<boolean>(false)
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total: number) => `共 ${total} 条记录`,
})

// 表格列定义
const columns = ref([
  {
    title: '目标标题',
    dataIndex: 'title',
    key: 'title',
    width: 250,
    ellipsis: true
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 90
  },
  {
    title: '开始日期',
    dataIndex: 'startDate',
    key: 'startDate',
    width: 180
  },
  {
    title: '结束日期',
    dataIndex: 'endDate',
    key: 'endDate',
    width: 180
  },
  {
    title: '进度',
    dataIndex: 'finalProgress',
    key: 'finalProgress',
    width: 120
  },
  {
    title: '延期状态',
    dataIndex: 'hasDelayedTasks',
    key: 'hasDelayedTasks',
    width: 100
  },
  {
    title: '预期时长',
    dataIndex: 'expectedDurationSec',
    key: 'expectedDurationSec',
    width: 120
  },
  {
    title: '实际时长',
    dataIndex: 'actualDurationSec',
    key: 'actualDurationSec',
    width: 120
  },
  {
    title: '超期时长',
    dataIndex: 'overdueCompletionTimeSec',
    key: 'overdueCompletionTimeSec',
    width: 120
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
    fixed: 'right'
  }
])

// 表格选择功能
const selectedRowKeys = ref<(string | number)[]>([])
const rowSelection = computed<TableProps['rowSelection']>(() => ({
  selectedRowKeys: selectedRowKeys.value,
  onChange: (selectedKeys: (string | number)[]) => {
    selectedRowKeys.value = selectedKeys
  }
}))

// 删除确认框相关
const openDeletePopconfirm = ref<boolean>(false)
const openPopconfirm = () => {
  if (!selectedRowKeys.value || selectedRowKeys.value.length === 0) {
    message.warning('请至少选择一条记录')
    return
  }
  openDeletePopconfirm.value = true
}
const closePopconfirm = () => {
  openDeletePopconfirm.value = false
}

// 重置搜索条件
const resetSearch = () => {
  Object.assign(searchForm, {
    title: undefined,
    status: undefined,
    startDate: undefined,
    endDate: undefined
  })
  handleSearch()
}

// 执行搜索
const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

// 处理分页
const handlePageChange = (page: number, pageSize: number) => {
  pagination.current = page
  pagination.pageSize = pageSize
  fetchData()
}

// 获取数据
const fetchData = async () => {
  try {
    loading.value = true
    const queryParam = {
      ...searchForm,
      pageNum: pagination.current,
      pageSize: pagination.pageSize
    }
    const response = await pageFocusGoal(queryParam)
    dataSource.value = response.data.records
    pagination.total = response.data.total
  } catch (err) {
    console.error('获取专注目标列表失败:', err)
    message.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// ========== 新增：分类相关状态 ==========
const categoryList = ref<FocusCategory[]>([])
const categoryLoading = ref<boolean>(false)
const categorySearchValue = ref<string>('')

// ========== 新增：标签相关状态 ==========
const tagList = ref<FocusTag[]>([])
const tagLoading = ref<boolean>(false)
const selectedTagIds = ref<number[]>([]) // 已选中的标签ID列表
const tagSearchValue = ref<string>('') // 标签搜索值

// 获取分类列表
const fetchCategoryList = async () => {
  try {
    categoryLoading.value = true
    const response = await listFocusCategory({ type: 'goal' })
    categoryList.value = response.data || []
  } catch (err) {
    console.error('获取分类列表失败:', err)
  } finally {
    categoryLoading.value = false
  }
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

// 新增：计算过滤后的分类选项
const filteredCategoryOptions = computed(() => {
  return categoryList.value.map(c => ({
    value: c.name,
    label: c.name,
    id: c.id
  }))
})

// 新增：过滤函数
const filterCategoryOption = (inputValue: string, option: any) => {
  return option.value.toLowerCase().includes(inputValue.toLowerCase())
}

// 修改：分类选择 - 根据名称查找ID
const handleCategorySelect = (value: string, option: any) => {
  categorySearchValue.value = value
  if (option && option.id) {
    modalForm.categoryId = option.id
  }
}

// 修改：快速新增分类
const handleCategoryAdd = async () => {
  if (!categorySearchValue.value || categorySearchValue.value.trim() === '') {
    message.warning('请输入分类名称')
    return
  }

  // 检查是否已存在
  const existCategory = categoryList.value.find(
    c => c.name === categorySearchValue.value.trim()
  )
  if (existCategory) {
    modalForm.categoryId = existCategory.id
    categorySearchValue.value = existCategory.name!
    message.info('该分类已存在，已自动选择')
    return
  }

  try {
    categoryLoading.value = true
    const newCategory: FocusCategory = {
      name: categorySearchValue.value.trim(),
      type: 'goal',
      color: '#1890ff'
    }

    const response = await saveFocusCategory(newCategory)

    // 检查响应状态码
    if (response.code === 200) {
      message.success('分类创建成功')

      // 重新加载分类列表
      await fetchCategoryList()

      // 自动选择新创建的分类
      const created = categoryList.value.find(c => c.name === newCategory.name)
      if (created) {
        modalForm.categoryId = created.id
        categorySearchValue.value = created.name!
      }
    } else {
      message.error(response.msg || '创建分类失败')
    }
  } catch (err) {
    console.error('创建分类失败:', err)
    message.error('创建分类失败')
  } finally {
    categoryLoading.value = false
  }
}

// 新增：标签选择变化
const handleTagChange = (values: number[]) => {
  selectedTagIds.value = values
}

// 新增：快速新增标签
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

// 模态框相关
const modalVisible = ref<boolean>(false)
const modalConfirmLoading = ref<boolean>(false)
const modalTitle = ref<string>('')
const isEdit = ref<boolean>(false)
const modalFormRef = ref()
const modalForm = reactive<FocusGoal>({
  title: '',
  description: '',
  categoryId: undefined,
  startDate: undefined,
  endDate: undefined,
  status: 'active',
  finalProgress: 0
})

// 计算属性：判断是否可以保存至草稿
// 只有新增或状态为草稿时才能保存至草稿，已开始的目标不能退回草稿
const canSaveAsDraft = computed(() => {
  return !isEdit.value || modalForm.status === 'draft'
})

// 时间格式化函数：将秒转换为可读格式
const formatDuration = (seconds?: number): string => {
  if (!seconds || seconds === 0) {
    return '0分钟'
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

  return parts.length > 0 ? parts.join('') : '0分钟'
}

// 表单验证规则
const modalRules = {
  title: [{ required: true, message: '请输入目标标题', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
}

// 新增操作
const handleAdd = () => {
  modalTitle.value = '新增专注目标'
  isEdit.value = false
  Object.assign(modalForm, {
    id: undefined,
    title: '',
    description: '',
    categoryId: undefined,
    startDate: undefined,
    endDate: undefined,
    status: 'active',
    finalProgress: 0
  })
  categorySearchValue.value = '' // 清空搜索值
  selectedTagIds.value = [] // 清空标签选择
  tagSearchValue.value = '' // 清空标签搜索值
  modalVisible.value = true
}

// 修改：编辑操作中的分类和标签显示
const handleEdit = async (record: FocusGoal) => {
  modalTitle.value = '编辑专注目标'
  isEdit.value = true
  modalVisible.value = true

  try {
    const response = await getFocusGoal(record.id!)
    Object.assign(modalForm, response.data)

    // 设置分类显示名称
    if (response.data.categoryId) {
      const category = categoryList.value.find(c => c.id === response.data.categoryId)
      categorySearchValue.value = category ? category.name! : ''
    } else {
      categorySearchValue.value = ''
    }

    // 设置已选中的标签（将字符串数组转换为数字数组）
    if (response.data.tagIds && response.data.tagIds.length > 0) {
      selectedTagIds.value = response.data.tagIds.map((id: string) => Number(id))
    } else {
      selectedTagIds.value = []
    }

    // 处理日期格式
    if (response.data.startDate) {
      modalForm.startDate = dayjs(response.data.startDate) as unknown as string
    }
    if (response.data.endDate) {
      modalForm.endDate = dayjs(response.data.endDate) as unknown as string
    }
  } catch (err) {
    console.error('获取专注目标详情失败:', err)
    message.error('获取详情失败')
  }
}

// 处理编辑点击事件（不在模板中直接使用 await）
const handleEditClick = (event: MouseEvent, record: FocusGoal) => {
  console.log('点击编辑按钮，记录:', record);
  event.stopPropagation()
  handleEdit(record)
}

// 处理详情点击事件 - 跳转到详情页面
const handleDetailClick = (event: MouseEvent, record: FocusGoal) => {
  console.log('点击详情按钮，记录:', record);
  event.stopPropagation()
  router.push(`/focus/goal/detail/${record.id}`)
}

// 保存目标的通用方法
const saveGoal = async (status: string) => {
  return modalFormRef.value
    .validate()
    .then(async () => {
      modalConfirmLoading.value = true
      try {
        // 处理日期格式
        const formData = { ...modalForm }
        if (formData.startDate instanceof dayjs) {
          formData.startDate = (formData.startDate as unknown as Dayjs).format('YYYY-MM-DD HH:mm:ss')
        }
        if (formData.endDate instanceof dayjs) {
          formData.endDate = (formData.endDate as unknown as Dayjs).format('YYYY-MM-DD HH:mm:ss')
        }

        // 设置状态
        formData.status = status

        // 添加标签ID（转换为字符串数组）
        formData.tagIds = selectedTagIds.value.map(id => String(id))

        await saveFocusGoal(formData)
        const statusText = status === 'draft' ? '草稿已保存' : status === 'active' ? '目标已开始' : '保存成功'
        message.success(statusText)
        modalVisible.value = false
        fetchData()
      } catch (err) {
        console.error(`${isEdit.value ? '编辑' : '新增'}专注目标失败:`, err)
        message.error(`${isEdit.value ? '编辑' : '新增'}失败`)
      } finally {
        modalConfirmLoading.value = false
      }
    })
    .catch(() => {
      // 表单验证失败
    })
}

// 保存至草稿
const handleSaveAsDraft = () => {
  saveGoal('draft')
}

// 开始目标（保存为进行中状态）
const handleSaveAsActive = () => {
  saveGoal('active')
}



// 模态框取消
const handleModalCancel = () => {
  modalVisible.value = false
}

// 删除单个记录
const handleDelete = async (id: number) => {
  try {
    await deleteFocusGoal([id])
    message.success('删除成功')
    fetchData()
  } catch (err) {
    console.error('删除专注目标失败:', err)
    message.error('删除失败')
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRowKeys.value.length === 0) {
    message.warning('请至少选择一条记录')
    return
  }

  try {
    await deleteFocusGoal(selectedRowKeys.value as number[])
    message.success('删除成功')
    selectedRowKeys.value = []
    fetchData()
  } catch (err) {
    console.error('批量删除专注目标失败:', err)
    message.error('删除失败')
  } finally {
    closePopconfirm()
  }
}

// 页面加载时获取数据和分类列表
onMounted(() => {
  fetchData()
  fetchCategoryList() // 加载分类列表
  fetchTagList() // 加载标签列表
})

// 解决 keep-alive 缓存问题，每次组件激活时重新查询数据
onActivated(() => {
  console.log('🔄 目标页面被激活，重新查询数据...')
  fetchData() // 重新查询目标数据
  fetchCategoryList() // 重新查询分类列表，避免缓存
  fetchTagList() // 重新查询标签列表，避免缓存
})
</script>

<style scoped lang="less">
.default-input-width {
  width: 160px;
}
</style>