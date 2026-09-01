<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="50%" >
    <ContentWrap>
      <!-- 搜索工作栏 -->
      <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="90px"
      >
        <el-form-item label="标签组名称" prop="groupName">
          <el-input
            v-model="queryParams.groupName"
            placeholder="请输入名称"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        </el-form-item>
      </el-form>
    </ContentWrap>

    <!-- 列表 -->
    <ContentWrap>
      <el-scrollbar height="400px" @end-reached ="onScroll">
        <div v-for="(item,index) in list" :key="index">
          <div class="font-bold flex items-center"><Icon icon="ep:caret-right" color="#409EFF" size="18" />{{item.groupName}}</div>
          <div class="py-3 px-2">
            <el-tag @click="selectTag(item2.name,item2.tagId)"  :type="selectedNames.includes(item2.name) ? 'primary' : 'info' " class="mr-2 cursor-pointer" v-for="(item2,index2) in item.tagsList" :key="index2">{{item2.name}}</el-tag>
          </div>
        </div>
      </el-scrollbar>
    </ContentWrap>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { CustomerTagGroupApi, CustomerTagGroupVO } from '@/api/crm/crmcustomertaggroup'


/** 客户标签分组 列表 */
defineOptions({ name: 'CrmCustomerTag' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<CustomerTagGroupVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 50,
  groupName: undefined,
  sort: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const isActive = ref(false)
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const selectedIds = ref([])
const selectedNames = ref([])

/** 打开弹窗 */
const open = async (arr) => {
  dialogVisible.value = true
  dialogTitle.value = '选择标签'
  list.value = []
  getList()
  selectedNames.value = arr
  
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

const onScroll = (direction) => {
  if(direction == 'bottom'){
    getList()
  }
}

const selectTag = (name,tagId) => {
  if(selectedNames.value.includes(name)){
    //message.warning('标签名重复,请重新选择')
    selectedNames.value = selectedNames.value.filter(element => element !== name);
    return
  }
  selectedNames.value.push(name)
  selectedIds.value.push(tagId)
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CustomerTagGroupApi.getCustomerTagGroupPage(queryParams)
    if(data.list && data.list.length > 0){
      list.value = list.value.concat(data.list)
    }
    //list.value = data.list
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

const emit = defineEmits(['success','successIds']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async  => {
  dialogVisible.value = false
    // 发送操作成功的事件
  emit('success',selectedNames.value)
  emit('successIds',selectedIds.value)
}



</script>