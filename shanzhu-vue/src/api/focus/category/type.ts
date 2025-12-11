import type { ApiResponsePage } from '@/api/types'

/**
 * 专注分类信息
 */
export interface FocusCategory {
  id?: number
  name?: string
  color?: string
  type?: string
  userId?: number
  createId?: number
  createTime?: string
  updateId?: number
  updateTime?: string
  delFlag?: string
}

/**
 * 专注分类查询参数
 */
export interface FocusCategoryQueryParams extends FocusCategory {
  pageNum: number
  pageSize: number
}

/**
 * 专注分类分页结果
 */
export type FocusCategoryPageResult = ApiResponsePage<FocusCategory>