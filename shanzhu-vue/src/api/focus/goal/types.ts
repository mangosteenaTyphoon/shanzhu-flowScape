import type { ApiResponsePage } from '@/api/types'

/**
 * 专注目标信息
 */
export interface FocusGoal {
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
  goalScore?: number // 目标综合评分（满分4分制）
  goalSummary?: string // 目标执行总结
  createdAt?: string
  updatedAt?: string
  tagIds?: string[] // 标签ID数组（用于保存）
}

/**
 * 专注目标查询参数
 */
export interface FocusGoalQueryParams extends FocusGoal {
  pageNum: number
  pageSize: number
}

/**
 * 专注目标分页结果
 */
export type FocusGoalPageResult = ApiResponsePage<FocusGoal>