<script lang="ts" setup>
import { formatDate } from '@/utils/formatTime'
import * as NotifyMessageApi from '@/api/system/notify/message'
import messageList from '@/views/system/notify/my/index.vue'

defineOptions({ name: 'Message' })

const { push,router } = useRouter()
const activeName = ref('notice')
const unreadCount = ref(0) // 未读消息数量
const list = ref<any[]>([]) // 消息列表
const drawer2 = ref(false)


// 获得消息列表
const getList = async () => {
  list.value = await NotifyMessageApi.getUnreadNotifyMessageList()
  // 强制设置 unreadCount 为 0，避免小红点因为轮询太慢，不消除
  unreadCount.value = 0
}

// 获得未读消息数
const getUnreadCount = async () => {
  NotifyMessageApi.getUnreadNotifyMessageCount().then((data) => {
    unreadCount.value = data
  })
}

const drawerRef = ref()
// 跳转我的站内信
const goMyList = () => {
  push({
    name: 'MyNotifyMessage'
  })
}

const openDrawer = () => {
  drawerRef.value.open()
}

// ========== 初始化 =========
onMounted(() => {
  // 首次加载小红点
  getUnreadCount()
  // 轮询刷新小红点
  setInterval(
    () => {
      getUnreadCount()
    },
    1000 * 60 * 2
  )
})
</script>
<template>
  <div class="message">
    <ElBadge :value="unreadCount" :show-zero="false" class="item">
          <Icon :size="18" class="cursor-pointer" icon="ep:bell" @click="openDrawer" />
    </ElBadge>
    <messageList ref="drawerRef" />
    <!-- <el-drawer v-model="drawer2" size="60%">
      <template #header>
        <h4>我的站内信</h4>
      </template>
      <template #default>
        <messageList />
      </template>
    </el-drawer> -->
  </div>
</template>
<style lang="scss" scoped>
.message-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 260px;
  line-height: 45px;
}

.message-list {
  display: flex;
  height: 400px;
  flex-direction: column;

  .message-item {
    display: flex;
    align-items: center;
    padding: 20px 0;
    border-bottom: 1px solid var(--el-border-color-light);

    &:last-child {
      border: none;
    }

    .message-icon {
      width: 40px;
      height: 40px;
      margin: 0 20px 0 5px;
    }

    .message-content {
      display: flex;
      flex-direction: column;

      .message-title {
        margin-bottom: 5px;
      }

      .message-date {
        font-size: 12px;
        color: var(--el-text-color-secondary);
      }
    }
  }
}
</style>
