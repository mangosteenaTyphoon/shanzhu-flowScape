import request from '@/utils/request'
import type { AxiosPromise } from 'axios'
import type { FocusGoal, FocusGoalQueryParams, FocusGoalPageResult } from './types'

/**
 * 分页查询专注目标列表
 * @param params 查询参数
 * @returns 分页数据
 */
export function pageFocusGoal(params: FocusGoalQueryParams): AxiosPromise<FocusGoalPageResult> {
  return request({
    url: '/focus/goal/page',
    method: 'post',
    data: params
  })
}

/**
 * 查询专注目标列表
 * @param params 查询参数
 * @returns 列表数据
 */
export function listFocusGoal(params?: FocusGoal): AxiosPromise<FocusGoal[]> {
  return request({
    url: '/focus/goal/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取专注目标详情
 * @param id 主键ID
 * @returns 专注目标详情
 */
export function getFocusGoal(id: number): AxiosPromise<FocusGoal> {
  return request({
    url: `/focus/goal/${id}`,
    method: 'get'
  })
}

/**
 * 保存专注目标
 * @param data 专注目标数据
 * @returns 保存结果
 */
export function saveFocusGoal(data: FocusGoal): AxiosPromise<boolean> {
  return request({
    url: '/focus/goal',
    method: 'post',
    data: data
  })
}

/**
 * 删除专注目标
 * @param ids 主键ID列表
 * @returns 删除结果
 */
export function deleteFocusGoal(ids: number[]): AxiosPromise<string> {
  return request({
    url: '/focus/goal',
    method: 'delete',
    data: ids
  })
}