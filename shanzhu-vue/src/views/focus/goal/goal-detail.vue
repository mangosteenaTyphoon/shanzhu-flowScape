<template>
  <div class="goal-detail-container">
    <!-- 页面标题栏 -->
    <a-page-header
      title="目标详情"
      @back="handleBack"
    >
      <template #extra>
        <a-button @click="handleBack">
          <template #icon>
            <RollbackOutlined />
          </template>
          返回
        </a-button>
      </template>
    </a-page-header>

    <a-spin :spinning="loading">
      <a-flex :gap="16" vertical style="padding: 16px">
        <!-- 目标详情信息 -->
        <a-card title="基本信息" :bordered="false">
          <a-descriptions :column="2" bordered>
            <a-descriptions-item label="目标标题" :span="2">
              {{ detailData.title }}
            </a-descriptions-item>
            <a-descriptions-item label="目标描述" :span="2">
              {{ detailData.description || '暂无描述' }}
            </a-descriptions-item>
            <a-descriptions-item label="目标状态">
              <a-tag v-if="detailData.status === 'draft'" color="default">草稿</a-tag>
              <a-tag v-else-if="detailData.status === 'active'" color="processing">进行中</a-tag>
              <a-tag v-else-if="detailData.status === 'completed'" color="success">已完成</a-tag>
              <a-tag v-else-if="detailData.status === 'archived'" color="warning">已归档</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="目标进度">
              <a-progress :percent="detailData.finalProgress || 0" :show-info="true" />
            </a-descriptions-item>
            <a-descriptions-item label="开始日期">
              {{ detailData.startDate ? dayjs(detailData.startDate).format('YYYY-MM-DD HH:mm:ss') : '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="结束日期">
              {{ detailData.endDate ? dayjs(detailData.endDate).format('YYYY-MM-DD HH:mm:ss') : '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="延期状态">
              <a-tag v-if="detailData.hasDelayedTasks" color="error">有延期任务</a-tag>
              <a-tag v-else color="success">无延期任务</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="预期时长">
              {{ formatDuration(detailData.expectedDurationSec) }}
            </a-descriptions-item>
            <a-descriptions-item label="实际时长">
              {{ formatDuration(detailData.actualDurationSec) }}
            </a-descriptions-item>
            <a-descriptions-item label="超期时长">
              <span v-if="detailData.overdueCompletionTimeSec && detailData.overdueCompletionTimeSec > 0"
                    style="color: #ff4d4f; font-weight: bold">
                {{ formatDuration(detailData.overdueCompletionTimeSec) }}
              </span>
              <span v-else style="color: #52c41a">无超期</span>
            </a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 任务列表 -->
        <a-card title="关联任务" :bordered="false">
          <a-table
            :columns="taskColumns"
            :data-source="taskList"
            :loading="taskLoading"
            :pagination="false"
            row-key="id"
          >
            <template #bodyCell="{ column, text, record }">
              <template v-if="column.key === 'title'">
                <a-typography-text ellipsis>{{ text }}</a-typography-text>
              </template>
              <template v-else-if="column.key === 'status'">
                <a-tag v-if="text === 'pending'" color="default">待开始</a-tag>
                <a-tag v-else-if="text === 'in_progress'" color="processing">进行中</a-tag>
                <a-tag v-else-if="text === 'completed'" color="success">已完成</a-tag>
                <a-tag v-else-if="text === 'paused'" color="warning">已暂停</a-tag>
              </template>
              <template v-else-if="column.key === 'priority'">
                <a-tag v-if="text === 'high'" color="red">高</a-tag>
                <a-tag v-else-if="text === 'medium'" color="orange">中</a-tag>
                <a-tag v-else-if="text === 'low'" color="blue">低</a-tag>
              </template>
              <template v-else-if="column.key === 'progressRate'">
                <a-progress :percent="text || 0" :show-info="false" size="small" />
                <span style="margin-left: 8px">{{ text || 0 }}%</span>
              </template>
              <template v-else-if="column.key === 'planStartDate' || column.key === 'planEndDate'">
                {{ text ? dayjs(text).format('YYYY-MM-DD HH:mm') : '-' }}
              </template>
              <template v-else-if="column.key === 'expectedDurationSec'">
                {{ formatDuration(text) }}
              </template>
              <template v-else-if="column.key === 'actualConsumedSec'">
                {{ formatDuration(text) }}
              </template>
            </template>
            <template #emptyText>
              <a-empty description="暂无关联任务" />
            </template>
          </a-table>
        </a-card>
      </a-flex>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { RollbackOutlined } from '@ant-design/icons-vue'

// 添加详细的调试日志
console.log('=== Goal Detail 组件开始加载 ===')

// API引入
import { getFocusGoal } from '@/api/focus/goal'
import { getFocusTaskList } from '@/api/focus/task'
import type { FocusTask } from '@/api/focus/task/types'

console.log('API 引入完成')

const route = useRoute()
const router = useRouter()

console.log('路由实例获取完成')
console.log('当前路由参数:', route.params)

// 状态管理
const loading = ref<boolean>(false)
const detailData = ref<FocusGoal>({})
const taskList = ref<FocusTask[]>([])
const taskLoading = ref<boolean>(false)

console.log('状态管理初始化完成')

// 任务列表表格列定义
const taskColumns = [
  {
    title: '任务标题',
    dataIndex: 'title',
    key: 'title',
    width: 200,
    ellipsis: true
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 90
  },
  {
    title: '优先级',
    dataIndex: 'priority',
    key: 'priority',
    width: 80
  },
  {
    title: '进度',
    dataIndex: 'progressRate',
    key: 'progressRate',
    width: 150
  },
  {
    title: '计划开始',
    dataIndex: 'planStartDate',
    key: 'planStartDate',
    width: 140
  },
  {
    title: '计划结束',
    dataIndex: 'planEndDate',
    key: 'planEndDate',
    width: 140
  },
  {
    title: '预期时长',
    dataIndex: 'expectedDurationSec',
    key: 'expectedDurationSec',
    width: 100
  },
  {
    title: '实际时长',
    dataIndex: 'actualConsumedSec',
    key: 'actualConsumedSec',
    width: 100
  }
]

console.log('表格列定义完成')

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

console.log('数据接口定义完成')

// 时间格式化函数：将秒转换为可读格式
const formatDuration = (seconds?: number): string => {
  console.log('调用 formatDuration，参数:', seconds)
  try {
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
  } catch (error) {
    console.error('formatDuration 函数执行出错:', error)
    return '格式化错误'
  }
}

console.log('formatDuration 函数定义完成')

// 返回上一页
const handleBack = () => {
  try {
    console.log('执行返回操作')
    router.back()
  } catch (error) {
    console.error('返回操作出错:', error)
  }
}

console.log('handleBack 函数定义完成')

// 加载目标详情和任务列表
const loadData = async () => {
  console.log('开始 loadData 函数')
  try {
    const goalId = route.params.id as string
    console.log('目标ID:', goalId)
    
    if (!goalId) {
      console.error('目标ID不存在')
      message.error('目标ID不存在')
      handleBack()
      return
    }

    console.log('开始设置加载状态')
    loading.value = true
    console.log('加载状态设置完成')

    // 获取目标详情
    console.log('准备调用 getFocusGoal 接口')
    const response = await getFocusGoal(Number(goalId))
    console.log('getFocusGoal 接口返回结果:', response)
    detailData.value = response.data
    console.log('detailData 设置完成:', detailData.value)

    // 获取关联的任务列表
    console.log('准备调用 getFocusTaskList 接口')
    taskLoading.value = true
    const taskResponse = await getFocusTaskList({ goalId: Number(goalId) })
    console.log('getFocusTaskList 接口返回结果:', taskResponse)
    taskList.value = taskResponse.data || []
    console.log('taskList 设置完成:', taskList.value)
  } catch (err) {
    console.error('获取目标详情失败:', err)
    message.error('获取详情失败')
  } finally {
    loading.value = false
    taskLoading.value = false
    console.log('数据加载完成，loading 状态已重置')
  }
}

console.log('loadData 函数定义完成')

// 页面加载时获取数据
onMounted(() => {
  console.log('=== Goal Detail 组件已挂载 ===')
  loadData()
})

console.log('onMounted 注册完成')
</script>

<style scoped lang="less">
.goal-detail-container {
  background: #f0f2f5;
  min-height: 100%;
}
</style>
