<template>
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
        <el-button @click="syncTag"><Icon icon="ep:refresh" class="mr-5px" /> 同步标签</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['crm:customer-tag-group:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <!-- <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['crm:customer-tag-group:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button> -->
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" row-key="id" :data="list" :stripe="true" :show-overflow-tooltip="true"  class="elTable" >
      <el-table-column label="排序" align="center" prop="groupName" width="60" >
        <template #default>
          <Icon icon="ep:operation" class="cursor-move" />
        </template>
      </el-table-column>
      <el-table-column label="分组名称" align="center" prop="groupName"  width="180"  />
      <el-table-column label="标签"  >
        <template #default="scope">
          <div>
            <el-tag class="mr-1" type="danger" v-for="(item,index) in scope.row.tagsList" :key="index" >{{item.name}}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          
          <el-button
            link
            type="primary"
            v-hasPermi="['crm:customer-tag-group:update']"
            @click="openForm('update', scope.row.id)"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['crm:customer-tag-group:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <CustomerTagGroupForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { CustomerTagGroupApi, CustomerTagGroupVO } from '@/api/crm/crmcustomertaggroup'
import CustomerTagGroupForm from './CustomerTagGroupForm.vue'
import Sortable from 'sortablejs'


/** 客户标签分组 列表 */
defineOptions({ name: 'CrmCustomerTagGroup' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<CustomerTagGroupVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  groupName: undefined,
  sort: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const isActive = ref(false)



const initSort = () => {
  console.log('list.value222:');
	const table = document.querySelector(".elTable .el-table__body-wrapper tbody");
	Sortable.create(table, {
		group: 'shared',
		animation: 150,
		ghostClass: 'sortable-ghost', //拖拽样式
		easing: 'cubic-bezier(1, 0, 0, 1)',
		onStart: (item: any) => {
			console.log(item);
		},
 
		// 结束拖动事件
		onEnd: ({ newDraggableIndex, oldDraggableIndex }) => {
      if (oldDraggableIndex !== newDraggableIndex) {
        list.value.splice(
          newDraggableIndex,
          0,
          list.value.splice(oldDraggableIndex, 1)[0]
        )
      }

      // var element = document.getElementById("myElement");
      // element.className=""
      let sortData = []
      list.value.map((item,index) => {
        let newData = {id:item.id,sort:index}
        sortData.push(newData)
        // item.sort = index
        // if(item.name == ''){
        //   message.warning('标签不能为空')
        //   return
        // }
      })
      updateSort(sortData)
      console.log('list.value:',list.value);
      console.log('lsortData:',sortData);
    }
	})
}

const updateSort = async(data)=>{
  await CustomerTagGroupApi.updateCustomerTagSort(data)
  message.success('排序成功')
  getList()
}

const syncTag = async()=>{
  await CustomerTagGroupApi.asynCustomerTagGroup()
  message.success('同步成功')
  getList()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CustomerTagGroupApi.getCustomerTagGroupPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await CustomerTagGroupApi.deleteCustomerTagGroup(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await CustomerTagGroupApi.exportCustomerTagGroup(queryParams)
    download.excel(data, '客户标签分组.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
  initSort()
})
</script>