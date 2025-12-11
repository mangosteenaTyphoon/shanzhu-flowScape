import request from '@/utils/request'
import type { AxiosPromise } from 'axios'
import type { FocusCategory, FocusCategoryQueryParams, FocusCategoryPageResult } from './types'

/**
 * 分页查询专注分类列表
 * @param params 查询参数
 * @returns 分页数据
 */
export function pageFocusCategory(params: FocusCategoryQueryParams): AxiosPromise<FocusCategoryPageResult> {
  return request({
    url: '/focus/category/page',
    method: 'post',
    data: params
  })
}

/**
 * 查询专注分类列表
 * @param params 查询参数
 * @returns 列表数据
 */
export function listFocusCategory(params?: FocusCategory): AxiosPromise<FocusCategory[]> {
  return request({
    url: '/focus/category/list',
    method: 'post',
    data: params || {}
  })
}

/**
 * 获取专注分类详情
 * @param id 主键ID
 * @returns 专注分类详情
 */
export function getFocusCategory(id: number): AxiosPromise<FocusCategory> {
  return request({
    url: `/focus/category/${id}`,
    method: 'get'
  })
}

/**
 * 保存专注分类
 * @param data 专注分类数据
 * @returns 保存结果
 */
export function saveFocusCategory(data: FocusCategory): AxiosPromise<boolean> {
  return request({
    url: '/focus/category',
    method: 'post',
    data: data
  })
}

/**
 * 删除专注分类
 * @param ids 主键ID列表
 * @returns 删除结果
 */
export function deleteFocusCategory(ids: number[]): AxiosPromise<string> {
  return request({
    url: '/focus/category',
    method: 'delete',
    data: ids
  })
}