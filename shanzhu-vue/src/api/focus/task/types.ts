import type { ApiResponsePage } from '@/api/types'

/**
 * 专注任务信息
 */
export interface FocusTask {
  id?: number
  userId?: number
  goalId?: number
  title?: string
  planStartDate?: string
  planEndDate?: string
  actualStartDate?: string
  actualEndDate?: string
  progressRate?: number
  weight?: number
  status?: string
  priority?: string
  expectedDurationSec?: number
  actualConsumedSec?: number
  createdAt?: string
  updatedAt?: string
}

/**
 * 专注任务查询参数
 */
export interface FocusTaskQueryParams extends FocusTask {
  pageNum: number
  pageSize: number
}

/**
 * 专注任务分页结果
 */
export type FocusTaskPageResult = ApiResponsePage<FocusTask>