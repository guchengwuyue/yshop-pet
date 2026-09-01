<template>
  <div class="home-dashboard">
    <el-row :gutter="16">
      <!-- 左侧主区域 -->
      <el-col :xs="24" :lg="16">
        <!-- 用户问候 & 快捷统计 -->
        <el-card class="dashboard-card mb-16px" shadow="never">
          <div class="greeting-card">
            <div class="greeting-left">
              <div class="greeting-text">{{ userName }}，{{ greetingText }}！</div>
              <div class="greeting-meta">
                <Icon icon="ep:sunny" :size="18" color="#f5a623" class="mr-6px" />
                <span>{{ mockData.weather.temp }}°C</span>
                <span class="meta-divider">|</span>
                <span>{{ currentDateText }}</span>
              </div>
            </div>
            <div class="greeting-stats">
              <div
                v-for="item in mockData.quickStats"
                :key="item.label"
                class="stat-item"
              >
                <div class="stat-value cursor-pointer" @click="toPath(item.path)">{{ item.value }}</div>
                <div class="stat-label">{{ item.label }}</div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 销售简报 -->
        <el-card class="dashboard-card mb-16px" shadow="never">
          <div class="card-header">
            <span class="card-title">系统简报</span>
            <!-- <div class="perf-tabs">
              <span
                :class="['perf-tab', { active: perfTab === 'my' }]"
                @click="handleSelect('my')"
              >本人简报</span>
              <span
                :class="['perf-tab', { active: perfTab === 'sub' }]"
                @click="handleSelect('sub')"
              >下属简报</span>
            </div> -->
          </div>
          <el-row :gutter="16" class="perf-grid">
            <el-col
              v-for="item in currentPerfStats"
              :key="item.label"
              :xs="12"
              :sm="6"
              class="perf-item"
            >
              <div class="perf-label">{{ item.label }}</div>
              <div class="perf-value">{{ item.value }}</div>
            </el-col>
          </el-row>
        </el-card>

        <!-- 常用功能 -->
        <el-card class="dashboard-card mb-16px" shadow="never">
          <div class="card-header">
            <span class="card-title">常用功能</span>
          </div>
          <div class="func-grid">
            <div
              v-for="item in mockData.commonFunctions"
              :key="item.label"
              class="func-item"
            >
              <router-link :to="item.path" >
                <div class="func-icon" :style="{ backgroundColor: item.color }">
                  <Icon :icon="item.icon" :size="24" color="#fff" />
                </div>
              </router-link>
              <span class="func-label">{{ item.label }}</span>
            
            </div>
            
          </div>
        </el-card>

        <!-- 我的站内信 -->
        <el-card class="dashboard-card" shadow="never">
          <div class="card-header">
            <span class="card-title">我的站内信</span>
          </div>
          <div class="notice-list">
            <div
              v-for="(item, index) in mockData.notifications"
              :key="index"
              class="notice-item"
            >
              <span class="notice-tag cursor-pointer" @click="toPath(item.pcUrl)">【{{ item.templateName }}】</span>
              <span class="notice-content">{{ item.templateContent }}</span>
              <span class="notice-time">{{ formatDate(item.createTime) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧区域 -->
      <el-col :xs="24" :lg="8">
        <!-- 欢迎横幅 -->
        <el-card class="dashboard-card welcome-card mb-16px" shadow="never">
          <div class="welcome-inner">
            <div class="welcome-illus">
              <Icon icon="ep:promotion" :size="48" color="#409eff" />
            </div>
            <div class="welcome-text">
              <div class="welcome-title">欢迎登录{{ appTitle }}</div>
              <div class="welcome-sub">高效团队铸就一流企业!!!</div>
            </div>
          </div>
        </el-card>

        <!-- 待办事项 -->
        <el-card class="dashboard-card mb-16px" style="height:375px" shadow="never">
          <div class="card-header">
            <span class="card-title">待办事项</span>
            <span class="more-link">今天</span>
          </div>
          <div class="calendar-strip">
            <Icon icon="ep:arrow-left" :size="14" color="#909399" class="calendar-arrow" />
            <div class="calendar-days">
              <div
                v-for="day in calendarDays"
                :key="day.date"
                class="calendar-day cursor-pointer"
                @click="handleSelectDay(day)"
              >
                <span class="day-week">{{ day.weekLabel }}</span>
                <span :class="['day-num', { active: day.isToday }]">{{ day.day }}</span>
              </div>
            </div>
            <Icon icon="ep:arrow-right" :size="14" color="#909399" class="calendar-arrow" />
          </div>
          <div class="todo-list">
            <div
              v-for="(item, index) in mockData.todoList"
              :key="index"
              class="todo-item"
            >
              <span class="todo-dot"></span>
              <span class="todo-text">{{ item.templateContent }}</span>
            </div>
            <div v-if="mockData.todoList.length === 0" ><el-empty image-size="60" description="暂无数据" /></div>
          </div>
        </el-card>

        <!-- 通知管理 -->
        <el-card class="dashboard-card" style="height:355px" shadow="never">
          <div class="card-header">
            <span class="card-title">通知管理</span>
          </div>
          <div class="dynamic-list">
            <div
              v-for="(item, index) in mockData.dynamics"
              :key="index"
              class="dynamic-item"
            >
              <div class="dynamic-title">{{ item.title }}</div>
              <div class="dynamic-meta">
                <Icon icon="ep:clock" :size="14" color="#909399"  />
                <span>{{ formatDate(item.createTime) }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/store/modules/user'
import axios from 'axios'
import router from '@/router'
import * as NoticeApi from '@/api/system/notice'
import { CustomerApi } from '@/api/crm/crmcustomer'
import { formatDate,beginOfDay,endOfDay } from '@/utils/formatTime'
import * as NotifyMessageApi from '@/api/system/notify/message'

const userStore = useUserStore()
const appTitle = import.meta.env.VITE_APP_TITLE || '库存系统'
const perfTab = ref<'my' | 'sub'>('my')

const weekLabels = ['日', '一', '二', '三', '四', '五', '六']

const count = ref({
  followCustomerCount: 0,
  followBusinessCount: 0,
  followCluesCount: 0,
  contractDueCount: 0,
  customerDueCount: 0,
  invoiceCheckCount: 0,
  receivablesCheckCount: 0,
  contractCheckCount: 0,
  canlendarCount: 0,
  planCount:0
})

const userName = computed(() => userStore.getUser.nickname || 'crm')

const greetingText = computed(() => {
  const hour = new Date().getHours()
  if (hour < 12) return '上午好'
  if (hour < 18) return '下午好'
  return '晚上好'
})

const currentDateText = computed(() => {
  const now = new Date()
  const y = now.getFullYear()
  const m = String(now.getMonth() + 1).padStart(2, '0')
  const d = String(now.getDate()).padStart(2, '0')
  return `${y}-${m}-${d} ${weekLabels[now.getDay()]}`
})

const selectedDate = ref('')

const calendarDays = computed(() => {
  const today = new Date()
  const todayStr = today.toISOString().slice(0, 10)
  const activeDate = selectedDate.value || todayStr
  const days: { weekLabel: string; day: number; date: string; isToday: boolean }[] = []
  for (let i = -3; i <= 3; i++) {
    const date = new Date(today)
    date.setDate(today.getDate() + i)
    const dateStr = date.toISOString().slice(0, 10)
    days.push({
      weekLabel: weekLabels[date.getDay()],
      day: date.getDate(),
      date: dateStr,
      isToday: dateStr === activeDate
    })
  }
  return days
})

const toPath = (redirect) => {
  router.push({ path: redirect || '/' })
}


const mockData = reactive({
  weather: { temp: 0 },
  quickStats: [
    { label: '入库待审核', value: count.value.todoCount },
    { label: '出库待审核', value: count.value.doneCount  }
  ],
  selfPerfStats: [
    { label: '今日入库', value: 0 },
    { label: '今日出库', value: 0 },
    { label: '商品数量', value: 0 },
    { label: '客户数量', value: 0 },
    { label: '供应商数', value: 0 },
    { label: '系统用户', value: 0 },
    { label: '商品分类', value: 0 },
    { label: '商品规格', value: 0 }
  ],
  commonFunctions: [
    { label: '商品入库', icon: 'ep:list', color: '#ff9f43',path: '/oa/stockin' },
    { label: '商品出库', icon: 'ep:office-building', color: '#a55eea',path: '/oa/stockout' },
    { label: '商品盘库', icon: 'ep:files', color: '#1dd1a1' ,path: '/oa/stockcheck'},
    { label: '库存查询', icon: 'ep:folder-opened', color: '#2e86de' ,path: '/oa/stocksearch'},
    { label: '客户管理', icon: 'ep:user', color: '#54a0ff',path: '/oa/customer' },
    { label: '供应商管理', icon: 'ep:shopping-cart', color: '#ff6b6b' ,path: '/oa/supplier'},
    { label: '商品管理', icon: 'ep:document-add', color: '#f368e0',path: '/shop/product/store-product' }
  ],
  notifications: [

  ],
  todoList: [

  ],
  dynamics: [
  ]
})

//获取本地天气
const getWeather = () => {
  // const url = 'https://api.openweathermap.org/data/2.5/weather'
  // const params = {
  //   q: '上海',
  //   appid: 'b1b15e88fa797225412429c1c50c122a',
  //   units: 'metric'
  // }
  // return axios.get(url, { params }).then((res) => {
  //   mockData.weather.temp = res.data.main.temp
  // })
}

getWeather()

const handleSelect = (tab) => {
  perfTab.value = tab
  getBiref()
}
const getData = async () => {
  try {
    const data = await CustomerApi.getIndexCount()

   // count.value = data
    mockData.quickStats = [
      { label: '入库待审核', value: data.todoCount,path: '/oa/stockin' },
      { label: '出库待审核', value: data.doneCount, path: '/oa/stockout' }
    ]
  } finally {
  }
}
getData()

const getBiref = async () => {
  try {
    const data = await CustomerApi.getIndexCount({relation:perfTab.value})
    mockData.selfPerfStats = [
      { label: '今日入库', value: data.brieCountVO.count03 },
      { label: '今日出库', value: data.brieCountVO.count08 },
      { label: '商品数量', value: data.brieCountVO.count01 },
      { label: '客户数量', value: data.brieCountVO.count05 },
      { label: '供应商数', value: data.brieCountVO.count06  },
      { label: '系统用户', value: data.brieCountVO.count07  },
      { label: '商品分类', value: data.brieCountVO.count02  },
      { label: '商品规格', value: data.brieCountVO.count04  }
    ]
    //count.value = data.brieCountVO
  } finally {
  }
}

getBiref()

const getNotice = async () => {
  try {
    const data = await NoticeApi.getNoticePage({
      pageNo: 1,
      pageSize: 5
    })
    mockData.dynamics = data.list
  } finally {
  }
}
getNotice()


const currentPerfStats = computed(() =>
  mockData.selfPerfStats
)

const getMsgList = async () => {
  try {
    const data = await NotifyMessageApi.getMyNotifyMessagePage({ pageNo: 1, pageSize: 7 })
    mockData.notifications = data.list
  } finally {
  }
}
getMsgList()

//跟进给定的日期获取当前日期开始时间与结束时间
const getDayRange = (date) => {
  const begin = beginOfDay(date)
  const end = endOfDay(date)
  return [formatDate(begin), formatDate(end)]
}

const getMsgList2 = async (date) => {
  try {
    const data = await NotifyMessageApi.getMyNotifyMessagePage(
      { pageNo: 1, 
        pageSize: 5,
        createTime: getDayRange(date),
        templateCode:['stock_warning','stockin_audit','stockout_audit'] 
      })
    mockData.todoList = data.list
  } finally {
  }
}
getMsgList2(new Date())

const handleSelectDay = (day) => {
  selectedDate.value = day.date
  getMsgList2(new Date(day.date))
}

</script>

<style lang="scss" scoped>
.home-dashboard {
  padding: 16px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 120px);
}

.dashboard-card {
  border-radius: 8px;
  border: none;

  :deep(.el-card__body) {
    padding: 20px;
  }
}

.mb-16px {
  margin-bottom: 16px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.more-link {
  font-size: 13px;
  color: #909399;
  cursor: pointer;

  &:hover {
    color: #409eff;
  }
}

.setting-icon {
  cursor: pointer;
}

/* 问候卡片 */
.greeting-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
}

.greeting-text {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.greeting-meta {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #909399;
}

.meta-divider {
  margin: 0 8px;
  color: #dcdfe6;
}

.greeting-stats {
  display: flex;
  align-items: center;
}

.stat-item {
  text-align: center;
  padding: 0 32px;
  border-right: 1px solid #ebeef5;

  &:last-child {
    border-right: none;
  }
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  line-height: 1.2;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

/* 业绩统计 */
.perf-tabs {
  display: flex;
  gap: 16px;
}

.perf-tab {
  font-size: 14px;
  color: #909399;
  cursor: pointer;
  padding-bottom: 2px;

  &.active {
    color: #409eff;
    font-weight: 500;
    border-bottom: 2px solid #409eff;
  }
}

.perf-grid {
  margin-top: 8px;
}

.perf-item {
  margin-bottom: 20px;
}

.perf-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 8px;
}

.perf-value {
  font-size: 24px;
  font-weight: 700;
  color: #303133;
}

/* 常用功能 */
.func-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 24px 16px;
}

.func-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: calc(100% / 7 - 16px);
  min-width: 72px;
  cursor: pointer;

  &:hover .func-icon {
    transform: scale(1.08);
  }
}

.func-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s;
}

.func-label {
  font-size: 12px;
  color: #606266;
  margin-top: 8px;
  text-align: center;
}

/* 系统通知 */
.notice-list {
  max-height: 280px;
  overflow-y: auto;
}

.notice-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f5f7fa;
  font-size: 13px;

  &:last-child {
    border-bottom: none;
  }
}

.notice-tag {
  color: #409eff;
  white-space: nowrap;
  flex-shrink: 0;
}

.notice-content {
  flex: 1;
  color: #606266;
  margin: 0 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notice-time {
  color: #909399;
  white-space: nowrap;
  flex-shrink: 0;
}

/* 欢迎横幅 */
.welcome-card {
  background: linear-gradient(135deg, #e8f4fd 0%, #d6ebfa 100%);

  :deep(.el-card__body) {
    padding: 24px;
  }
}

.welcome-inner {
  display: flex;
  align-items: center;
  gap: 16px;
}

.welcome-illus {
  flex-shrink: 0;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 50%;
}

.welcome-title {
  font-size: 18px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 6px;
}

.welcome-sub {
  font-size: 13px;
  color: #606266;
}

/* 待办日历 */
.calendar-strip {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding: 8px 0;
}

.calendar-arrow {
  cursor: pointer;
  flex-shrink: 0;
}

.calendar-days {
  display: flex;
  flex: 1;
  justify-content: space-around;
}

.calendar-day {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.day-week {
  font-size: 12px;
  color: #909399;
}

.day-num {
  width: 28px;
  height: 28px;
  line-height: 28px;
  text-align: center;
  font-size: 14px;
  color: #606266;
  border-radius: 50%;

  &.active {
    background-color: #409eff;
    color: #fff;
    font-weight: 600;
  }
}

.todo-list {
  border-top: 1px solid #f5f7fa;
  padding-top: 8px;
}

.todo-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  font-size: 13px;
  color: #606266;
}

.todo-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: #409eff;
  margin-right: 10px;
  flex-shrink: 0;
}

.todo-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 企业动态 */
.dynamic-list {
  max-height: 320px;
  overflow-y: auto;
}

.dynamic-item {
  padding: 12px 0;
  border-bottom: 1px solid #f5f7fa;

  &:last-child {
    border-bottom: none;
  }
}

.dynamic-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dynamic-meta {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #909399;
}

.meta-num {
  margin-left: 4px;
}

.ml-12px {
  margin-left: 12px;
}

@media (max-width: 992px) {
  .greeting-stats {
    width: 100%;
    justify-content: space-around;
  }

  .stat-item {
    padding: 0 16px;
  }

  .func-item {
    width: calc(25% - 12px);
  }
}
</style>
