<template>
  <div class="goal-detail-container">
    <!-- 页面标题栏 -->
    <a-page-header
      title="目标详情"
      @back="handleBack"
      style="padding-top: 4px; padding-bottom: 8px"
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
      <a-flex :gap="12" vertical style="padding: 12px; padding-top: 0">
        <!-- 目标详情信息 -->
        <a-card title="基本信息" :bordered="false" size="small">
          <a-descriptions :column="2" bordered size="small">
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
        <a-card title="关联任务" :bordered="false" size="small">
          <template #extra>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <PlusOutlined />
              </template>
              新增任务
            </a-button>
          </template>

          <!-- 搜索筛选区域 -->
          <a-form :colon="false" style="margin-bottom: 12px">
            <a-row :gutter="12">
              <a-col>
                <a-form-item label="任务标题">
                  <a-input v-model:value="searchForm.title" placeholder="请输入任务标题" allow-clear style="width: 160px" />
                </a-form-item>
              </a-col>
              <a-col>
                <a-form-item label="任务状态">
                  <a-select v-model:value="searchForm.status" placeholder="请选择任务状态" allow-clear style="width: 120px">
                    <a-select-option value="todo">待办</a-select-option>
                    <a-select-option value="in_progress">进行中</a-select-option>
                    <a-select-option value="done">已完成</a-select-option>
                    <a-select-option value="cancelled">已取消</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col>
                <a-form-item label="计划开始日期">
                  <a-date-picker
                    v-model:value="searchForm.planStartDate"
                    placeholder="请选择日期"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    show-time
                    style="width: 200px"
                  />
                </a-form-item>
              </a-col>
              <a-col>
                <a-form-item label="计划结束日期">
                  <a-date-picker
                    v-model:value="searchForm.planEndDate"
                    placeholder="请选择日期"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    show-time
                    style="width: 200px"
                  />
                </a-form-item>
              </a-col>
              <a-col>
                <a-form-item>
                  <a-space size="small">
                    <a-button type="primary" @click="handleSearch" :loading="taskLoading">
                      <template #icon>
                        <SearchOutlined />
                      </template>
                      查询
                    </a-button>
                    <a-button @click="resetSearch" :loading="taskLoading">
                      <template #icon>
                        <RedoOutlined />
                      </template>
                      重置
                    </a-button>
                  </a-space>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <a-table
            :columns="taskColumns"
            :data-source="taskList"
            :loading="taskLoading"
            :pagination="pagination"
            :scroll="{ x: 1400 }"
            row-key="id"
            @change="handleTableChange"
          >
            <template #bodyCell="{ column, text, record }">
              <template v-if="column.key === 'title'">
                <a-typography-text ellipsis>{{ text }}</a-typography-text>
              </template>
              <template v-else-if="column.key === 'status'">
                <a-tag v-if="text === 'todo'" color="default">待办</a-tag>
                <a-tag v-else-if="text === 'in_progress'" color="processing">进行中</a-tag>
                <a-tag v-else-if="text === 'done'" color="success">已完成</a-tag>
                <a-tag v-else-if="text === 'completedOverdueAllowed'" color="orange">超期完成</a-tag>
                <a-tag v-else-if="text === 'completedOverdue'" color="error">逾期完成</a-tag>
                <a-tag v-else-if="text === 'cancelled'" color="warning">已取消</a-tag>
              </template>
              <template v-else-if="column.key === 'qualityGrade'">
                <a-tag v-if="text === 'A'" color="green">A-优秀</a-tag>
                <a-tag v-else-if="text === 'B'" color="blue">B-良好</a-tag>
                <a-tag v-else-if="text === 'C'" color="orange">C-合格</a-tag>
                <a-tag v-else-if="text === 'D'" color="red">D-不及格</a-tag>
                <span v-else style="color: #bfbfbf">未评级</span>
              </template>

              <template v-else-if="column.key === 'progressRate'">
                <a-progress :percent="text || 0" :show-info="false" size="small" />
                <span style="margin-left: 8px">{{ text || 0 }}%</span>
              </template>
              <template v-else-if="column.key === 'planStartDate' || column.key === 'planEndDate' || column.key === 'actualStartDate' || column.key === 'actualEndDate'">
                {{ text ? dayjs(text).format('YYYY-MM-DD HH:mm') : '-' }}
              </template>
              <template v-else-if="column.key === 'expectedDurationSec'">
                {{ formatDuration(text) }}
              </template>
              <template v-else-if="column.key === 'actualConsumedSec'">
                {{ formatDuration(text) }}
              </template>
              <template v-else-if="column.key === 'taskSummary'">
                <a-typography-text
                  ellipsis
                  :content="text || '暂无总结'"
                  style="cursor: pointer; color: #1890ff"
                  @click="handleViewTaskSummary(record)"
                >
                  {{ text || '暂无总结' }}
                </a-typography-text>
              </template>
              <template v-else-if="column.key === 'action'">
                <a-space size="small">
                  <!-- 根据任务状态显示不同操作按钮 -->

                  <!-- todo状态：显示开始按钮 -->
                  <a-button
                    v-if="record.status === 'todo'"
                    type="primary"
                    size="small"
                    @click="handleStartTask(record)"
                  >
                    <template #icon>
                      <PlayCircleOutlined />
                    </template>
                    开始
                  </a-button>

                  <!-- in_progress状态：显示修改进度按钮 -->
                  <a-button
                    v-if="record.status === 'in_progress'"
                    type="primary"
                    size="small"
                    @click="handleUpdateProgress(record)"
                  >
                    <template #icon>
                      <PercentageOutlined />
                    </template>
                    修改进度
                  </a-button>

                  <!-- 完成状态：显示输入总结按钮 -->
                  <a-button
                    v-if="record.status === 'done' || record.status === 'completedOverdue' || record.status === 'completedOverdueAllowed'"
                    type="primary"
                    size="small"
                    @click="handleEditSummary(record)"
                  >
                    <template #icon>
                      <EditOutlined />
                    </template>
                    输入总结
                  </a-button>

                  <!-- 通用编辑按钮（非完成状态时显示） -->
                  <a-button
                    v-if="record.status !== 'done' && record.status !== 'completedOverdue' && record.status !== 'completedOverdueAllowed'"
                    type="link"
                    size="small"
                    @click="handleEdit(record)"
                  >
                    <template #icon>
                      <EditOutlined />
                    </template>
                    编辑
                  </a-button>

                  <!-- 删除按钮 -->
                  <a-popconfirm
                    title="确定要删除这条任务吗？"
                    ok-text="确认"
                    cancel-text="取消"
                    @confirm="handleDelete([record.id])"
                    :disabled="record.status === 'done' || record.status === 'completedOverdue' || record.status === 'completedOverdueAllowed'"
                  >
                    <a-button
                      type="link"
                      size="small"
                      danger
                      :disabled="record.status === 'done' || record.status === 'completedOverdue' || record.status === 'completedOverdueAllowed'"
                    >
                      <template #icon>
                        <DeleteOutlined />
                      </template>
                      删除
                    </a-button>
                  </a-popconfirm>
                </a-space>
              </template>
            </template>
            <template #emptyText>
              <a-empty description="暂无关联任务" />
            </template>
          </a-table>
        </a-card>
      </a-flex>
    </a-spin>

    <!-- 新增/编辑任务模态框 -->
    <a-modal
      v-model:open="modalVisible"
      :confirm-loading="modalConfirmLoading"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
      :width="600"
    >
      <template #title>
        <div style="margin-bottom: 24px">
          <a-typography-title :level="4">{{ modalTitle }}</a-typography-title>
        </div>
      </template>

      <a-form
        layout="vertical"
        :model="modalForm"
        :rules="modalRules"
        ref="modalFormRef"
      >
        <a-form-item label="任务标题" name="title">
          <a-input v-model:value="modalForm.title" placeholder="请输入任务标题" />
        </a-form-item>

        <a-form-item label="所属目标" name="goalId">
          <a-input-group compact>
            <a-input
              v-model:value="selectedGoalTitle"
              placeholder="请选择目标"
              :disabled="true"
              style="width: calc(100% - 80px)"
            />
            <a-button @click="handleOpenGoalModal" style="width: 80px">选择目标</a-button>
          </a-input-group>
        </a-form-item>

        <a-form-item label="任务描述">
          <a-textarea
            v-model:value="modalForm.description"
            placeholder="请输入任务描述"
            :rows="4"
          />
        </a-form-item>

        <a-form-item label="标签">
          <a-select
            v-model:value="selectedTagIds"
            mode="multiple"
            placeholder="请选择标签"
            :loading="tagLoading"
            allow-clear
            @change="handleTagChange"
          >
            <template #dropdownRender="{ menuNode: menu }">
              <div>
                <component :is="menu" />
                <a-divider style="margin: 4px 0" />
                <div style="padding: 8px; display: flex; gap: 8px">
                  <a-input
                    v-model:value="tagSearchValue"
                    placeholder="输入新标签名称"
                    @pressEnter="handleTagAdd"
                  />
                  <a-button type="primary" @click="handleTagAdd">
                    <template #icon>
                      <PlusOutlined />
                    </template>
                    新增
                  </a-button>
                </div>
              </div>
            </template>
            <a-select-option
              v-for="tag in tagList"
              :key="tag.id"
              :value="tag.id"
            >
              <a-tag :color="tag.color">{{ tag.name }}</a-tag>
            </a-select-option>
          </a-select>
        </a-form-item>



        <a-form-item label="权重">
          <a-input-number
            v-model:value="modalForm.weight"
            :min="0"
            :max="100"
            style="width: 100%"
          />
        </a-form-item>

        <a-form-item label="计划时间" required>
          <div style="display: flex; align-items: center; gap: 8px">
            <a-form-item name="planStartDate" :wrapper-col="{ span: 24 }" style="margin-bottom: 0; flex: 1">
              <a-date-picker
                v-model:value="modalForm.planStartDate"
                placeholder="开始时间"
                show-time
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </a-form-item>
            <span style="color: #bfbfbf; user-select: none; padding: 0 8px; margin-top: -24px">——</span>
            <a-form-item name="planEndDate" :wrapper-col="{ span: 24 }" style="margin-bottom: 0; flex: 1">
              <a-date-picker
                v-model:value="modalForm.planEndDate"
                placeholder="结束时间"
                show-time
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </a-form-item>
          </div>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 目标选择弹窗 -->
    <a-modal
      v-model:open="goalModalVisible"
      title="选择目标"
      @ok="goalModalVisible = false"
      @cancel="goalModalVisible = false"
      width="800px"
    >
      <a-table
        :columns="[
          { title: '目标标题', dataIndex: 'title', key: 'title' },
          { title: '状态', dataIndex: 'status', key: 'status', width: 100 },
          { title: '操作', key: 'action', width: 100 }
        ]"
        :data-source="goalList"
        :loading="goalLoading"
        :pagination="false"
        row-key="id"
        size="small"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.key === 'status'">
            <a-tag v-if="text === 'draft'" color="default">草稿</a-tag>
            <a-tag v-else-if="text === 'active'" color="processing">进行中</a-tag>
            <a-tag v-else-if="text === 'completed'" color="success">已完成</a-tag>
            <a-tag v-else-if="text === 'archived'" color="warning">已归档</a-tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-button type="link" size="small" @click="handleSelectGoal(record)">
              选择
            </a-button>
          </template>
        </template>
      </a-table>
    </a-modal>

    <!-- 状态时间输入弹窗 -->
    <a-modal
      v-model:open="statusTimeModalVisible"
      :title="statusTimeModalTitle"
      :confirm-loading="statusTimeModalLoading"
      @ok="handleStatusTimeModalOk"
      @cancel="handleStatusTimeModalCancel"
      width="400px"
    >
      <a-form>
        <a-form-item label="时间">
          <a-date-picker
            v-model:value="statusTimeValue"
            placeholder="请选择时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            show-time
            style="width: 100%"
          />
        </a-form-item>
        <a-alert
          v-if="statusTimeModalTitle.includes('开始')"
          message="任务将变更为「进行中」状态"
          type="info"
          show-icon
          style="margin-top: 8px"
        />
        <a-alert
          v-else
          message="任务将变更为「已完成」状态"
          type="success"
          show-icon
          style="margin-top: 8px"
        />
      </a-form>
    </a-modal>

    <!-- 进度修改弹窗 -->
    <a-modal
      v-model:open="progressModalVisible"
      title="修改任务进度"
      :confirm-loading="progressModalLoading"
      @ok="handleProgressModalOk"
      @cancel="handleProgressModalCancel"
      width="500px"
    >
      <a-form layout="vertical">
        <a-form-item label="任务标题">
          <a-input :value="currentTaskRecord?.title" disabled />
        </a-form-item>

        <a-form-item label="当前进度">
          <div style="padding: 0 10px">
            <a-slider
              v-model:value="progressValue"
              :min="0"
              :max="100"
              :marks="{ 0: '0%', 25: '25%', 50: '50%', 75: '75%', 100: '100%' }"
              :tooltip="{ formatter: (value) => `${value}%` }"
            />
          </div>
          <div style="margin-top: 8px; text-align: center">
            <a-input-number
              v-model:value="progressValue"
              :min="0"
              :max="100"
              :precision="0"
              style="width: 120px"
              addon-after="%"
            />
          </div>
        </a-form-item>

        <a-alert
          v-if="progressValue === 100"
          message="进度达到100%将自动完成任务，请准备输入完成时间"
          type="warning"
          show-icon
          style="margin-top: 8px"
        />
        <a-alert
          v-else
          message="可以随时调整任务进度，支持多次修改"
          type="info"
          show-icon
          style="margin-top: 8px"
        />
      </a-form>
    </a-modal>

    <!-- 任务总结编辑弹窗 -->
    <a-modal
      v-model:open="summaryModalVisible"
      title="编辑任务总结"
      :confirm-loading="summaryModalLoading"
      @ok="handleSummaryModalOk"
      @cancel="handleSummaryModalCancel"
      width="600px"
    >
      <a-form layout="vertical">
        <a-form-item label="任务标题">
          <a-input :value="currentTaskRecord?.title" disabled />
        </a-form-item>

        <a-form-item label="质量等级">
          <a-select v-model:value="summaryQualityGrade" placeholder="请选择质量等级" allow-clear>
            <a-select-option value="A">A - 优秀</a-select-option>
            <a-select-option value="B">B - 良好</a-select-option>
            <a-select-option value="C">C - 合格</a-select-option>
            <a-select-option value="D">D - 不及格</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="任务总结">
          <a-textarea
            v-model:value="summaryValue"
            placeholder="请输入任务总结，包括完成情况、遇到的问题、收获等"
            :rows="6"
          />
        </a-form-item>

        <a-alert
          message="您可以随时修改任务总结和质量等级评价"
          type="info"
          show-icon
          style="margin-top: 8px"
        />
      </a-form>
    </a-modal>

    <!-- 任务总结查看弹窗 -->
    <a-modal
      v-model:open="viewSummaryModalVisible"
      title="查看任务总结"
      :footer="null"
      width="700px"
      :body-style="{ maxHeight: '70vh', overflow: 'hidden', padding: '16px' }"
    >
      <div style="max-height: 60vh; overflow-y: auto; padding-right: 8px;">
        <a-descriptions :column="1" bordered size="small">
          <a-descriptions-item label="任务标题">
            <div style="word-wrap: break-word; word-break: break-all;">
              {{ viewSummaryTaskRecord?.title || '无标题' }}
            </div>
          </a-descriptions-item>
          <a-descriptions-item label="质量等级">
            <a-tag v-if="viewSummaryTaskRecord?.qualityGrade === 'A'" color="green">A - 优秀</a-tag>
            <a-tag v-else-if="viewSummaryTaskRecord?.qualityGrade === 'B'" color="blue">B - 良好</a-tag>
            <a-tag v-else-if="viewSummaryTaskRecord?.qualityGrade === 'C'" color="orange">C - 合格</a-tag>
            <a-tag v-else-if="viewSummaryTaskRecord?.qualityGrade === 'D'" color="red">D - 不及格</a-tag>
            <span v-else style="color: #bfbfbf">未评级</span>
          </a-descriptions-item>
          <a-descriptions-item label="任务总结" :span="1">
            <div
              v-if="viewSummaryTaskRecord?.taskSummary"
              style="
                white-space: pre-wrap;
                line-height: 1.6;
                padding: 12px;
                background-color: #ffffff;
                border-radius: 6px;
                border: 1px solid #e8e8e8;
                max-height: 400px;
                overflow-y: auto;
                word-wrap: break-word;
                word-break: break-all;
                font-size: 14px;
                position: relative;
              "
            >
              <!-- 全屏按钮 -->
              <a-button
                type="text"
                size="small"
                @click="handleFullscreenSummary"
                style="
                  position: absolute;
                  top: 8px;
                  right: 8px;
                  z-index: 10;
                  background-color: rgba(255, 255, 255, 0.9);
                  border: none;
                  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                "
                :title="'全屏查看'"
              >
                <template #icon>
                  <FullscreenOutlined />
                </template>
              </a-button>

              {{ viewSummaryTaskRecord.taskSummary }}
            </div>
            <div v-else style="color: #bfbfbf; font-style: italic; text-align: center; padding: 20px;">
              暂无总结
            </div>
          </a-descriptions-item>
        </a-descriptions>
      </div>

      <!-- 提示文本 -->
      <div v-if="viewSummaryTaskRecord?.taskSummary && viewSummaryTaskRecord.taskSummary.length > 200"
           style="margin-top: 8px; text-align: center; color: #999; font-size: 12px;">
        📖 内容较长，可上下滚动查看完整总结
      </div>
    </a-modal>

    <!-- 全屏任务总结弹窗 -->
    <a-modal
      v-model:open="fullscreenSummaryModalVisible"
      title="全屏查看任务总结"
      :footer="null"
      :width="'100vw'"
      :style="{ top: 0 }"
      :body-style="{
        height: 'calc(100vh - 55px)',
        overflow: 'hidden',
        padding: 0,
        margin: 0
      }"
      :mask="false"
      :closable="true"
      :keyboard="true"
    >
      <div style="
        height: 100%;
        display: flex;
        flex-direction: column;
        background-color: #f8f9fa;
      ">
        <!-- 头部信息区域 -->
        <div style="
          padding: 24px 32px;
          background-color: #ffffff;
          border-bottom: 1px solid #e8e8e8;
          flex-shrink: 0;
        ">
          <a-descriptions :column="2" bordered size="default" style="margin-bottom: 0;">
            <a-descriptions-item label="任务标题" :span="2">
              <div style="font-size: 16px; font-weight: 500; word-wrap: break-word; word-break: break-all;">
                {{ viewSummaryTaskRecord?.title || '无标题' }}
              </div>
            </a-descriptions-item>
            <a-descriptions-item label="质量等级">
              <a-tag v-if="viewSummaryTaskRecord?.qualityGrade === 'A'" color="green" style="font-size: 14px; padding: 4px 8px;">A - 优秀</a-tag>
              <a-tag v-else-if="viewSummaryTaskRecord?.qualityGrade === 'B'" color="blue" style="font-size: 14px; padding: 4px 8px;">B - 良好</a-tag>
              <a-tag v-else-if="viewSummaryTaskRecord?.qualityGrade === 'C'" color="orange" style="font-size: 14px; padding: 4px 8px;">C - 合格</a-tag>
              <a-tag v-else-if="viewSummaryTaskRecord?.qualityGrade === 'D'" color="red" style="font-size: 14px; padding: 4px 8px;">D - 不及格</a-tag>
              <span v-else style="color: #bfbfbf; font-size: 14px;">未评级</span>
            </a-descriptions-item>
            <a-descriptions-item label="字数统计">
              <span style="color: #666; font-size: 14px;">
                {{ viewSummaryTaskRecord?.taskSummary ? `${viewSummaryTaskRecord.taskSummary.length} 字符` : '0 字符' }}
              </span>
            </a-descriptions-item>
          </a-descriptions>
        </div>

        <!-- 总结内容区域 -->
        <div style="
          flex: 1;
          padding: 32px;
          overflow-y: auto;
          background-color: #ffffff;
        ">
          <div
            v-if="viewSummaryTaskRecord?.taskSummary"
            style="
              white-space: pre-wrap;
              line-height: 1.8;
              font-size: 16px;
              color: #333;
              word-wrap: break-word;
              word-break: break-all;
              max-width: none;
              letter-spacing: 0.5px;
            "
          >
            {{ viewSummaryTaskRecord.taskSummary }}
          </div>
          <div v-else style="
            color: #bfbfbf;
            font-style: italic;
            text-align: center;
            padding: 60px 20px;
            font-size: 18px;
          ">
            <div style="font-size: 48px; margin-bottom: 16px;">📝</div>
            <div>暂无任务总结</div>
            <div style="font-size: 14px; margin-top: 8px; color: #999;">
              该任务尚未添加总结内容
            </div>
          </div>
        </div>

        <!-- 底部操作区域 -->
        <div style="
          padding: 16px 32px;
          background-color: #fafafa;
          border-top: 1px solid #e8e8e8;
          text-align: center;
          flex-shrink: 0;
        ">
          <a-button @click="fullscreenSummaryModalVisible = false" size="large">
            关闭全屏
          </a-button>
          <div style="margin-top: 8px; color: #999; font-size: 12px;">
            按 ESC 键或点击右上角 ✕ 也可关闭全屏
          </div>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import dayjs from 'dayjs'
import { RollbackOutlined, PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined, RedoOutlined, PlayCircleOutlined, PercentageOutlined, EyeOutlined, FullscreenOutlined } from '@ant-design/icons-vue'
import type { FormInstance } from 'ant-design-vue'

// 添加详细的调试日志
console.log('=== Goal Detail 组件开始加载 ===')

// API引入
import { getFocusGoal } from '@/api/focus/goal'
import { getFocusTaskList, getFocusTask, saveFocusTask, deleteFocusTask } from '@/api/focus/task'
import type { FocusTask } from '@/api/focus/task/types'
import { listFocusTag, saveFocusTag } from '@/api/focus/tag'
import type { FocusTag } from '@/api/focus/tag/types'
import { listFocusGoal } from '@/api/focus/goal'
import type { FocusGoal as FocusGoalType } from '@/api/focus/goal/types'

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
const allTaskList = ref<FocusTask[]>([]) // 保存所有任务数据，用于筛选
const filteredTaskList = ref<FocusTask[]>([]) // 筛选后的任务数据

// 分页相关状态
const pagination = reactive({
  current: 1,        // 当前页码
  pageSize: 10,      // 每页显示数量
  total: 0,          // 总条数
  showSizeChanger: true,  // 显示页面大小选择器
  showQuickJumper: true,  // 显示快速跳转
  showTotal: (total: number, range: [number, number]) => `共 ${total} 条，当前 ${range[0]}-${range[1]} 条`,
  pageSizeOptions: ['5', '10', '20', '50', '100'], // 页面大小选项
})

// 搜索表单
const searchForm = reactive({
  title: undefined as string | undefined,
  status: undefined as string | undefined,
  planStartDate: undefined as string | undefined,
  planEndDate: undefined as string | undefined
})

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
    width: 120
  },
  {
    title: '质量等级',
    dataIndex: 'qualityGrade',
    key: 'qualityGrade',
    width: 90
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
    title: '实际开始',
    dataIndex: 'actualStartDate',
    key: 'actualStartDate',
    width: 140
  },
  {
    title: '实际结束',
    dataIndex: 'actualEndDate',
    key: 'actualEndDate',
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
  },
  {
    title: '任务总结',
    dataIndex: 'taskSummary',
    key: 'taskSummary',
    width: 200,
    ellipsis: true
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 200
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

// ========== 标签相关状态 ==========
const tagList = ref<FocusTag[]>([])
const tagLoading = ref<boolean>(false)
const selectedTagIds = ref<number[]>([]) // 已选中的标签ID列表
const tagSearchValue = ref<string>('') // 标签搜索值

// ========== 目标相关状态 ==========
const goalList = ref<FocusGoalType[]>([])
const goalLoading = ref<boolean>(false)
const goalModalVisible = ref<boolean>(false) // 目标选择弹窗
const selectedGoalId = ref<number | undefined>(undefined)
const selectedGoalTitle = ref<string>('')

// 模态框相关
const modalVisible = ref<boolean>(false)
const modalConfirmLoading = ref<boolean>(false)
const modalTitle = ref<string>('')
const isEdit = ref<boolean>(false)
const modalFormRef = ref<FormInstance>()
const modalForm = reactive<FocusTask>({
  title: '',
  goalId: undefined,
  weight: 100,
  status: 'todo',
  priority: 'medium',
  planStartDate: undefined,
  planEndDate: undefined,
  actualStartDate: undefined,
  actualEndDate: undefined,
  progressRate: 0,
  qualityGrade: undefined,
  taskSummary: ''
})

// 表单验证规则
const modalRules = {
  title: [{ required: true, message: '请输入任务标题', trigger: 'blur' }],
  goalId: [{ required: true, message: '请选择所属目标', trigger: 'change' }],
  planStartDate: [{ required: true, message: '请选择计划开始日期', trigger: 'change' }],
  planEndDate: [{ required: true, message: '请选择计划结束日期', trigger: 'change' }]
}

// ========== 状态时间输入弹窗相关 ==========
const statusTimeModalVisible = ref<boolean>(false)
const statusTimeModalLoading = ref<boolean>(false)
const statusTimeModalTitle = ref<string>('')
const statusTimeValue = ref<string>()
const pendingStatusChange = ref<{ record: FocusTask, newStatus: string } | null>(null)

// ========== 进度修改弹窗相关 ==========
const progressModalVisible = ref<boolean>(false)
const progressModalLoading = ref<boolean>(false)
const progressValue = ref<number>(0)
const currentTaskRecord = ref<FocusTask | null>(null)

// ========== 任务总结编辑弹窗相关 ==========
const summaryModalVisible = ref<boolean>(false)
const summaryModalLoading = ref<boolean>(false)
const summaryValue = ref<string>('')
const summaryQualityGrade = ref<string>('')

// ========== 任务总结查看弹窗相关 ==========
const viewSummaryModalVisible = ref<boolean>(false)
const viewSummaryTaskRecord = ref<FocusTask | null>(null)

// ========== 全屏任务总结弹窗相关 ==========
const fullscreenSummaryModalVisible = ref<boolean>(false)

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
    // 按创建时间排序（最新的在前面）
    const sortedTasks = (taskResponse.data || []).sort((a, b) => {
      const timeA = a.createdAt ? new Date(a.createdAt).getTime() : 0
      const timeB = b.createdAt ? new Date(b.createdAt).getTime() : 0
      return timeB - timeA
    })
    allTaskList.value = sortedTasks
    filteredTaskList.value = sortedTasks
    updatePaginationData()
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

// 获取目标列表
const fetchGoalList = async () => {
  try {
    goalLoading.value = true
    const response = await listFocusGoal({})
    goalList.value = response.data || []
  } catch (err) {
    console.error('获取目标列表失败:', err)
  } finally {
    goalLoading.value = false
  }
}

// 统一的数据刷新函数 - 在所有后端操作后调用
const refreshAllData = async () => {
  try {
    console.log('开始刷新所有数据')
    // 并行执行所有数据刷新操作
    await Promise.all([
      loadData(),        // 刷新目标详情和任务列表
      fetchTagList(),    // 刷新标签列表
      fetchGoalList()    // 刷新目标列表
    ])
    console.log('所有数据刷新完成')
  } catch (err) {
    console.error('数据刷新失败:', err)
    message.error('数据刷新失败，请手动刷新页面')
  }
}

// 标签选择变化
const handleTagChange = (values: number[]) => {
  selectedTagIds.value = values
}

// 快速新增标签
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

    if (response.code === 200) {
      message.success('标签创建成功')
      await refreshAllData()

      const created = tagList.value.find(t => t.name === newTag.name)
      if (created && !selectedTagIds.value.includes(created.id!)) {
        selectedTagIds.value.push(created.id!)
      }

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

// 打开目标选择弹窗
const handleOpenGoalModal = () => {
  goalModalVisible.value = true
}

// 选择目标
const handleSelectGoal = (goal: FocusGoalType) => {
  selectedGoalId.value = goal.id
  selectedGoalTitle.value = goal.title || ''
  modalForm.goalId = goal.id
  goalModalVisible.value = false
}

// 新增操作
const handleAdd = () => {
  modalTitle.value = '新增专注任务'
  isEdit.value = false
  Object.assign(modalForm, {
    id: undefined,
    title: '',
    goalId: Number(route.params.id), // 自动关联当前目标
    weight: 100,
    status: 'todo',
    priority: 'medium',
    planStartDate: undefined,
    planEndDate: undefined,
    actualStartDate: undefined,
    actualEndDate: undefined,
    progressRate: 0,
    qualityGrade: undefined,
    taskSummary: ''
  })
  selectedTagIds.value = []
  tagSearchValue.value = ''
  selectedGoalId.value = Number(route.params.id)
  selectedGoalTitle.value = detailData.value.title || ''
  modalVisible.value = true
}

// 编辑操作
const handleEdit = async (record: FocusTask) => {
  modalTitle.value = '编辑专注任务'
  isEdit.value = true
  modalVisible.value = true

  try {
    const response = await getFocusTask(record.id!)
    Object.assign(modalForm, response.data)

    // 设置已选中的标签
    if (response.data.tagIds && response.data.tagIds.length > 0) {
      selectedTagIds.value = response.data.tagIds.map((id: string) => Number(id))
    } else {
      selectedTagIds.value = []
    }

    // 设置目标信息
    if (response.data.goalId) {
      selectedGoalId.value = response.data.goalId
      if (response.data.goal && response.data.goal.title) {
        selectedGoalTitle.value = response.data.goal.title
      } else {
        const goal = goalList.value.find(g => g.id === response.data.goalId)
        selectedGoalTitle.value = goal ? goal.title || '' : `目标ID: ${response.data.goalId}`
      }
    } else {
      selectedGoalId.value = undefined
      selectedGoalTitle.value = ''
    }

    // 处理日期格式
    if (response.data.planStartDate) {
      modalForm.planStartDate = dayjs(response.data.planStartDate) as unknown as string
    }
    if (response.data.planEndDate) {
      modalForm.planEndDate = dayjs(response.data.planEndDate) as unknown as string
    }
  } catch (err) {
    console.error('获取专注任务详情失败:', err)
    message.error('获取详情失败')
  }
}

// 模态框确认
const handleModalOk = () => {
  modalFormRef.value
    ?.validate()
    .then(async () => {
      modalConfirmLoading.value = true
      try {
        const formData = { ...modalForm }

        // 进度与状态同步逻辑
        if (formData.status === 'done') {
          formData.progressRate = 100
        }
        if (formData.progressRate === 100 && formData.status !== 'done' && formData.status !== 'cancelled') {
          formData.status = 'done'
        }

        // 处理日期格式
        if (formData.planStartDate) {
          formData.planStartDate = dayjs(formData.planStartDate).format('YYYY-MM-DD HH:mm:ss')
        }
        if (formData.planEndDate) {
          formData.planEndDate = dayjs(formData.planEndDate).format('YYYY-MM-DD HH:mm:ss')
        }

        // 添加标签ID
        formData.tagIds = selectedTagIds.value.map(id => String(id))

        const result = await saveFocusTask(formData)
        if (result.code === 200 && result.data) {
          message.success(`${isEdit.value ? '编辑' : '新增'}成功`)
          modalVisible.value = false
          await refreshAllData() // 刷新所有相关数据
        } else {
          message.error(result.msg || `${isEdit.value ? '编辑' : '新增'}失败`)
        }
      } catch (err) {
        console.error('保存专注任务失败:', err)
        message.error('保存失败')
      } finally {
        modalConfirmLoading.value = false
      }
    })
    .catch(() => {
      // 表单验证失败
    })
}

// 模态框取消
const handleModalCancel = () => {
  modalVisible.value = false
  modalFormRef.value?.resetFields()
}

// 删除操作
const handleDelete = (ids: number[]) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除选中的${ids.length}条记录吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        const result = await deleteFocusTask(ids)
        if (result.code === 200 && result.data) {
          message.success('删除成功')
          await refreshAllData() // 刷新所有相关数据
        } else {
          message.error(result.msg || '删除失败')
        }
      } catch (err) {
        console.error('删除专注任务失败:', err)
        message.error('删除失败')
      }
    }
  })
}

// 快速修改任务状态
const handleStatusChange = async (record: FocusTask, newStatus: string) => {
  // 如果状态变为进行中，需要输入实际开始时间
  if (newStatus === 'in_progress') {
    statusTimeModalTitle.value = '请输入实际开始时间'
    statusTimeValue.value = undefined
    pendingStatusChange.value = { record, newStatus }
    statusTimeModalVisible.value = true
    return
  }

  // 如果状态变为已完成，需要输入实际结束时间
  if (newStatus === 'done') {
    statusTimeModalTitle.value = '请输入实际结束时间'
    statusTimeValue.value = undefined
    pendingStatusChange.value = { record, newStatus }
    statusTimeModalVisible.value = true
    return
  }

  // 其他状态直接更新
  await updateTaskStatus(record, newStatus)
}

// 实际更新任务状态的方法
const updateTaskStatus = async (record: FocusTask, newStatus: string, timeValue?: string) => {
  try {
    const updateData: FocusTask = {
      id: record.id,
      status: newStatus,
      title: record.title,
      goalId: record.goalId,
      weight: record.weight,
      priority: record.priority,
      planStartDate: record.planStartDate,
      planEndDate: record.planEndDate,
      actualStartDate: record.actualStartDate,
      actualEndDate: record.actualEndDate,
      progressRate: record.progressRate,
      qualityGrade: record.qualityGrade,
      taskSummary: record.taskSummary
    }

    // 如果是进行中，设置实际开始时间
    if (newStatus === 'in_progress' && timeValue) {
      updateData.actualStartDate = timeValue
    }

    // 如果是已完成，设置实际结束时间和进度 100%
    if (newStatus === 'done' || newStatus === 'completedOverdue') {
      if (timeValue) {
        updateData.actualEndDate = timeValue
      }
      updateData.progressRate = 100
    }

    const result = await saveFocusTask(updateData)
    if (result.code === 200 && result.data) {
      message.success('状态更新成功')
      await refreshAllData()
    } else {
      message.error(result.msg || '状态更新失败')
      await refreshAllData()
    }
  } catch (err) {
    console.error('更新任务状态失败:', err)
    message.error('状态更新失败')
    loadData()
  }
}

// 确认时间输入
const handleStatusTimeModalOk = async () => {
  if (!statusTimeValue.value) {
    message.warning('请选择时间')
    return
  }

  if (!pendingStatusChange.value) {
    return
  }

  statusTimeModalLoading.value = true
  try {
    await updateTaskStatus(
      pendingStatusChange.value.record,
      pendingStatusChange.value.newStatus,
      statusTimeValue.value
    )
    statusTimeModalVisible.value = false
  } finally {
    statusTimeModalLoading.value = false
  }
}

// 取消时间输入
const handleStatusTimeModalCancel = () => {
  statusTimeModalVisible.value = false
  pendingStatusChange.value = null
  statusTimeValue.value = undefined
}

// 搜索查询
const handleSearch = () => {
  let filteredList = [...allTaskList.value]

  // 按标题筛选
  if (searchForm.title && searchForm.title.trim()) {
    filteredList = filteredList.filter(task =>
      task.title?.toLowerCase().includes(searchForm.title!.toLowerCase())
    )
  }

  // 按状态筛选
  if (searchForm.status) {
    filteredList = filteredList.filter(task => task.status === searchForm.status)
  }

  // 按计划开始日期筛选
  if (searchForm.planStartDate) {
    filteredList = filteredList.filter(task => {
      if (!task.planStartDate) return false
      return dayjs(task.planStartDate).isAfter(dayjs(searchForm.planStartDate)) ||
             dayjs(task.planStartDate).isSame(dayjs(searchForm.planStartDate))
    })
  }

  // 按计划结束日期筛选
  if (searchForm.planEndDate) {
    filteredList = filteredList.filter(task => {
      if (!task.planEndDate) return false
      return dayjs(task.planEndDate).isBefore(dayjs(searchForm.planEndDate)) ||
             dayjs(task.planEndDate).isSame(dayjs(searchForm.planEndDate))
    })
  }

  filteredTaskList.value = filteredList
  pagination.current = 1 // 搜索后重置到第一页
  updatePaginationData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.title = undefined
  searchForm.status = undefined
  searchForm.planStartDate = undefined
  searchForm.planEndDate = undefined
  filteredTaskList.value = [...allTaskList.value]
  pagination.current = 1 // 重置到第一页
  updatePaginationData()
}

// 分页处理函数
const handleTableChange = (paginationInfo: any) => {
  pagination.current = paginationInfo.current
  pagination.pageSize = paginationInfo.pageSize
  updatePaginationData()
}

// 更新分页数据
const updatePaginationData = () => {
  const startIndex = (pagination.current - 1) * pagination.pageSize
  const endIndex = startIndex + pagination.pageSize
  taskList.value = filteredTaskList.value.slice(startIndex, endIndex)
  pagination.total = filteredTaskList.value.length
}

// ========== 新增的任务操作函数 ==========

// 开始任务
const handleStartTask = (record: FocusTask) => {
  statusTimeModalTitle.value = '请输入实际开始时间'
  statusTimeValue.value = dayjs().format('YYYY-MM-DD HH:mm:ss') // 默认当前时间
  pendingStatusChange.value = { record, newStatus: 'in_progress' }
  statusTimeModalVisible.value = true
}

// 修改进度
const handleUpdateProgress = (record: FocusTask) => {
  currentTaskRecord.value = record
  progressValue.value = record.progressRate || 0
  progressModalVisible.value = true
}

// 查看详情（完成状态任务）
const handleViewDetail = (record: FocusTask) => {
  handleEdit(record) // 复用编辑逻辑，但可以设为只读模式
}

// 进度弹窗确认
const handleProgressModalOk = async () => {
  if (!currentTaskRecord.value) {
    return
  }

  progressModalLoading.value = true
  try {
    const updateData: FocusTask = {
      id: currentTaskRecord.value.id,
      title: currentTaskRecord.value.title,
      goalId: currentTaskRecord.value.goalId,
      weight: currentTaskRecord.value.weight,
      priority: currentTaskRecord.value.priority,
      planStartDate: currentTaskRecord.value.planStartDate,
      planEndDate: currentTaskRecord.value.planEndDate,
      actualStartDate: currentTaskRecord.value.actualStartDate,
      actualEndDate: currentTaskRecord.value.actualEndDate,
      status: currentTaskRecord.value.status,
      progressRate: progressValue.value
    }

    // 如果进度达到100%，需要处理完成逻辑
    if (progressValue.value === 100) {
      // 关闭进度弹窗，打开完成时间输入弹窗
      progressModalVisible.value = false
      statusTimeModalTitle.value = '请输入实际完成时间'
      statusTimeValue.value = dayjs().format('YYYY-MM-DD HH:mm:ss')

      // 判断是否逾期
      const isOverdue = currentTaskRecord.value.planEndDate &&
                       dayjs().isAfter(dayjs(currentTaskRecord.value.planEndDate))

      pendingStatusChange.value = {
        record: { ...updateData },
        newStatus: isOverdue ? 'completedOverdue' : 'done'
      }
      statusTimeModalVisible.value = true
      return
    }

    // 如果进度未达到100%，直接更新进度
    const result = await saveFocusTask(updateData)
    if (result.code === 200 && result.data) {
      message.success('进度更新成功')
      progressModalVisible.value = false
      await refreshAllData()
    } else {
      message.error(result.msg || '进度更新失败')
    }
  } catch (err) {
    console.error('更新任务进度失败:', err)
    message.error('进度更新失败')
  } finally {
    progressModalLoading.value = false
  }
}

// 进度弹窗取消
const handleProgressModalCancel = () => {
  progressModalVisible.value = false
  progressValue.value = 0
  currentTaskRecord.value = null
}

// 编辑任务总结
const handleEditSummary = (record: FocusTask) => {
  currentTaskRecord.value = record
  summaryValue.value = record.taskSummary || ''
  summaryQualityGrade.value = record.qualityGrade || ''
  summaryModalVisible.value = true
}

// 任务总结弹窗确认
const handleSummaryModalOk = async () => {
  if (!currentTaskRecord.value) {
    return
  }

  summaryModalLoading.value = true
  try {
    const updateData: FocusTask = {
      id: currentTaskRecord.value.id,
      title: currentTaskRecord.value.title,
      goalId: currentTaskRecord.value.goalId,
      weight: currentTaskRecord.value.weight,
      priority: currentTaskRecord.value.priority,
      planStartDate: currentTaskRecord.value.planStartDate,
      planEndDate: currentTaskRecord.value.planEndDate,
      actualStartDate: currentTaskRecord.value.actualStartDate,
      actualEndDate: currentTaskRecord.value.actualEndDate,
      status: currentTaskRecord.value.status,
      progressRate: currentTaskRecord.value.progressRate,
      qualityGrade: summaryQualityGrade.value,
      taskSummary: summaryValue.value
    }

    const result = await saveFocusTask(updateData)
    if (result.code === 200 && result.data) {
      message.success('任务总结更新成功')
      summaryModalVisible.value = false
      await refreshAllData()
    } else {
      message.error(result.msg || '任务总结更新失败')
    }
  } catch (err) {
    console.error('更新任务总结失败:', err)
    message.error('任务总结更新失败')
  } finally {
    summaryModalLoading.value = false
  }
}

// 任务总结弹窗取消
const handleSummaryModalCancel = () => {
  summaryModalVisible.value = false
  summaryValue.value = ''
  summaryQualityGrade.value = ''
  currentTaskRecord.value = null
}

// 查看任务总结详情
const handleViewTaskSummary = (record: FocusTask) => {
  viewSummaryTaskRecord.value = record
  viewSummaryModalVisible.value = true
}

// 全屏查看任务总结
const handleFullscreenSummary = () => {
  fullscreenSummaryModalVisible.value = true
}

// 页面加载时获取数据
onMounted(() => {
  console.log('=== Goal Detail 组件已挂载 ===')
  loadData()
  fetchTagList()
  fetchGoalList()
})

console.log('onMounted 注册完成')
</script>

<style scoped lang="less">
.goal-detail-container {
  background: #f0f2f5;
  min-height: 100%;
}

.status-select-wrapper {
  position: relative;
  width: 100px;

  .ant-select {
    opacity: 0;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 2;
  }

  .status-display {
    position: relative;
    z-index: 1;
    pointer-events: none;
    display: flex;
    align-items: center;
    height: 24px;

    .ant-tag {
      user-select: none;
    }
  }

  &:hover {
    .status-display .ant-tag {
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      transform: translateY(-1px);
      transition: all 0.2s ease;
    }
  }
}
</style>
