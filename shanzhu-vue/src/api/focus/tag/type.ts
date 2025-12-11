import type { ApiResponsePage } from '@/api/types'

/**
 * 专注标签信息
 */
export interface FocusTag {
  id?: number
  name?: string
  color?: string
  userId?: number
  createId?: number
  createTime?: string
  updateId?: number
  updateTime?: string
  delFlag?: string
}

/**
 * 专注标签查询参数
 */
export interface FocusTagQueryParams extends FocusTag {
  pageNum: number
  pageSize: number
}

/**
 * 专注标签分页结果
 */
export type FocusTagPageResult = ApiResponsePage<FocusTag>
