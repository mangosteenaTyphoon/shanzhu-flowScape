import request from '@/utils/request'
import type { AxiosPromise } from 'axios'
import type { FocusTag, FocusTagQueryParams, FocusTagPageResult } from './types'

/**
 * 分页查询专注标签列表
 * @param params 查询参数
 * @returns 分页数据
 */
export function pageFocusTag(params: FocusTagQueryParams): AxiosPromise<FocusTagPageResult> {
  return request({
    url: '/focus/tag/page',
    method: 'post',
    data: params
  })
}

/**
 * 查询专注标签列表
 * @param params 查询参数
 * @returns 列表数据
 */
export function listFocusTag(params?: FocusTag): AxiosPromise<FocusTag[]> {
  return request({
    url: '/focus/tag/list',
    method: 'post',
    data: params || {}
  })
}

/**
 * 获取专注标签详情
 * @param id 主键ID
 * @returns 专注标签详情
 */
export function getFocusTag(id: number): AxiosPromise<FocusTag> {
  return request({
    url: `/focus/tag/${id}`,
    method: 'get'
  })
}

/**
 * 保存专注标签
 * @param data 专注标签数据
 * @returns 保存结果
 */
export function saveFocusTag(data: FocusTag): AxiosPromise<boolean> {
  return request({
    url: '/focus/tag',
    method: 'post',
    data: data
  })
}

/**
 * 删除专注标签
 * @param ids 主键ID列表
 * @returns 删除结果
 */
export function deleteFocusTag(ids: number[]): AxiosPromise<string> {
  return request({
    url: '/focus/tag',
    method: 'delete',
    data: ids
  })
}
