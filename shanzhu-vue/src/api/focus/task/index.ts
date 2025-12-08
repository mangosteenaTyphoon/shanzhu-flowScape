import request from '@/utils/request'
import type { AxiosPromise } from 'axios'
import type { FocusTask, FocusTaskPageResult, FocusTaskQueryParams } from './types'

/**
 * 分页查询专注任务
 * @param params 查询参数
 * @returns 分页结果
 */
export function getFocusTaskPage(params: FocusTaskQueryParams): AxiosPromise<FocusTaskPageResult> {
  return request({
    url: '/focus/task/page',
    method: 'post',
    data: params
  })
}

/**
 * 查询专注任务列表
 * @param params 查询参数
 * @returns 列表结果
 */
export function getFocusTaskList(params?: FocusTask): AxiosPromise<FocusTask[]> {
  return request({
    url: '/focus/task/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取专注任务详情
 * @param id 主键ID
 * @returns 专注任务信息
 */
export function getFocusTask(id: number): AxiosPromise<FocusTask> {
  return request({
    url: `/focus/task/${id}`,
    method: 'get'
  })
}

/**
 * 保存专注任务
 * @param data 专注任务信息
 * @returns 是否成功
 */
export function saveFocusTask(data: FocusTask): AxiosPromise<boolean> {
  return request({
    url: '/focus/task',
    method: 'post',
    data: data
  })
}

/**
 * 删除专注任务
 * @param ids 主键ID列表
 * @returns 是否成功
 */
export function deleteFocusTask(ids: number[]): AxiosPromise<boolean> {
  return request({
    url: '/focus/task',
    method: 'delete',
    data: ids
  })
}