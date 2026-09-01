<template>


  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column label="操作人" align="center" prop="userName" width="120" />
      <el-table-column label="操作内容" align="center" prop="action" />
      <el-table-column
        label="操作时间"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import * as OperateLogApi from '@/api/system/operatelog'


defineOptions({ name: 'CrmOperateLog' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  userId: undefined,
  type: undefined,
  subType: undefined,
  action: undefined,
  createTime: [],
  bizId: undefined
})


const props = defineProps<{
  bizId: 0,
  type: "CRM_CLUE"
}>()



/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await OperateLogApi.getOperateLogPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}


/** 初始化 **/
onMounted(async () => {
  queryParams.bizId = props.bizId
  queryParams.type = props.type
  await getList()
})
</script>
