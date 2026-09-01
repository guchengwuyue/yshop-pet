<template>
  <ContentWrap>
    <el-menu
    :default-active="activeIndex"
    style="margin-bottom:10px"
    mode="horizontal"
    @select="handleSelect"
  >
      <el-menu-item index="configOne">客户跟进配置</el-menu-item>
      <el-menu-item index="configTwo">客户公海配置</el-menu-item>
      <el-menu-item index="configThree">线索池配置</el-menu-item>
    </el-menu>

    <el-card v-if="activeIndex == 'configOne'">
      <template #header>
        <div class="font-bold">
          <span>客户跟进规则</span>
        </div>
      </template>
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        :label-position="labelPosition"
      >
        <el-form-item label="未成交提醒" prop="noDealDay">
          <el-input-number v-model="formData.noDealDay"  :min="0" :max="99" placeholder="请输入天数"  class="!w-340px" />
          <el-alert :closable="false" title="用于设置暂未成交状态的客户多少天需要跟进一次，0不提醒" type="warning" style="margin-top:2px" />
        </el-form-item>
        <el-form-item label="已经成交提醒" prop="dealDay">
          <el-input-number v-model="formData.dealDay"  :min="0" :max="10" placeholder="请输入天数" class="!w-340px" />
          <el-alert :closable="false" title="用于设置已成交状态的客户多少天需要跟进一次,0不提醒" type="warning" style="margin-top:2px" />
        </el-form-item>
        <el-form-item label="">
          <el-button @click="submitForm(1)" type="primary">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="activeIndex == 'configTwo'">
      <template #header>
        <div class="font-bold">
          <span>客户公海</span>
        </div>
      </template>
      <el-form
        ref="formRef"
        :model="formData1"
        :rules="formRules"
        :label-position="labelPosition"
      >
        <el-form-item label="未成交退回" prop="noDealDay">
          <el-input-number v-model="formData1.noDealDay"  :min="0" :max="99" placeholder="请输入天数"  class="!w-340px" />
          <el-alert :closable="false" title="用于设置客户在未成交状态滞留多少天自动退回公海，0不退回" type="warning" style="margin-top:2px" />
        </el-form-item>
        <el-form-item label="未跟进退回" prop="noFollowDay">
          <el-input-number v-model="formData1.noFollowDay"  :min="0" :max="10" placeholder="请输入天数" class="!w-340px" />
          <el-alert :closable="false" title="用于设置未成交状态的客户，多少天未跟进自动退回公海,0不退回" type="warning" style="margin-top:2px" />
        </el-form-item>
        <el-form-item label="退回公海提醒" prop="frontDay">
          <el-input-number v-model="formData1.frontDay"  :min="0" :max="10" placeholder="请输入天数" class="!w-340px" />
          <el-alert :closable="false" title="用于客户退回公海提前多少天进行提醒,0不提醒" type="warning" style="margin-top:2px" />
        </el-form-item>
        <el-form-item label="拥有客户数量" prop="haveNum">
          <el-input-number v-model="formData1.haveNum"  :min="0" :max="10" placeholder="请输入天数" class="!w-340px" />
          <el-alert :closable="false" title="用于设置每个员工最多可以拥有多少个暂未成交状态的客户,0不限制" type="warning" style="margin-top:2px" />
        </el-form-item>
        <el-form-item label="">
          <el-button @click="submitForm(2)" type="primary">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="activeIndex == 'configThree'">
      <template #header>
        <div class="font-bold">
          <span>线索池规则</span>
        </div>
      </template>
      <el-form
        ref="formRef"
        :model="formData2"
        :rules="formRules"
        :label-position="labelPosition"
      >
        <el-form-item label="未转客户退回" prop="noDealDay">
          <el-input-number v-model="formData2.noDealDay"  :min="0" :max="99" placeholder="请输入天数"  class="!w-340px" />
          <el-alert :closable="false" title="用于设置线索多少天未转客户，自动退回线索池，0不退回" type="warning"  />
        </el-form-item>
        <el-form-item label="未跟进退回" prop="noFollowDay">
          <el-input-number v-model="formData2.noFollowDay"  :min="0" :max="10" placeholder="请输入天数" class="!w-340px" />
          <el-alert :closable="false" title="用于设置线索多少天未跟进，自动退回线索池,0不退回" type="warning" style="margin-top:2px" />
        </el-form-item>
        <el-form-item label="退回线索池提醒" prop="frontDay">
          <el-input-number v-model="formData2.frontDay"  :min="0" :max="10" placeholder="请输入天数" class="!w-340px" />
          <el-alert :closable="false" title="用于设置线索退回线索池，提前多少天进行提醒,0不提醒" type="warning" style="margin-top:2px" />
        </el-form-item>
        <el-form-item label="拥有线索数量" prop="haveNum">
          <el-input-number v-model="formData2.haveNum"  :min="0" :max="10" placeholder="请输入天数" class="!w-340px" />
          <el-alert :closable="false" title="用于设置每个销售人员最多可以拥有多少条线索,0不限制" type="warning" style="margin-top:2px" />
        </el-form-item>
        <el-form-item label="">
          <el-button @click="submitForm(3)" type="primary">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
  </ContentWrap>
  <!-- </Dialog> -->
</template>
<script setup lang="ts">
import { CustomerApi } from '@/api/crm/crmcustomer'

/** 企业微信配置 表单 */
defineOptions({ name: 'WorkWxConfigForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  noDealDay: 0,
  dealDay: 0,
})
const formData1 = ref({
  noDealDay: 0,
  noFollowDay: 0,
  frontDay: 0,
  haveNum: 0,
})
const formData2 = ref({
  noDealDay: 0,
  noFollowDay: 0,
  frontDay: 0,
  haveNum: 0,
})
const formRules = reactive({
  noDealDay: [{ required: true, message: '可拥有客户不能为空', trigger: 'blur' }],
  dealDay: [{ required: true, message: '不跟进自动公海天数不能为空', trigger: 'blur' }],
  noFollowDay: [{ required: true, message: '不成交自动公海天数不能为空', trigger: 'blur' }],
  frontDay: [{ required: true, message: '可拥有客户不能为空', trigger: 'blur' }],
  haveNum: [{ required: true, message: '不跟进自动公海天数不能为空', trigger: 'blur' }],
})
const formRef = ref() // 表单 Ref
const activeIndex = ref('configOne')
const labelPosition = ref<FormProps['labelPosition']>('top')

const handleSelect = (key) => {
  activeIndex.value = key
  if(key == 'configOne'){
    getConfig(1)
  }else if(key == 'configTwo'){
    getConfig(2)
  }else if(key == 'configThree'){
    getConfig(3)
  }
}

const submitForm = async (type) => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    let data = formData.value
    if(type == 2){
      data = formData1.value
    }else if(type == 3){
      data = formData2.value
    }
    await CustomerApi.saveRedisSet(type,data)
    message.success('配置成功')
    dialogVisible.value = false
    // 发送操作成功的事件
    
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {
    customerNum: 0,
    notRecordDay: 0,
    notSuccessDay: 0,
  }
  formRef.value?.resetFields()
}

const getConfig = async (type) => {
  const data = await CustomerApi.getRedisSet(type)
  if(data){
    if(type == 1){
      formData.value = data
    }else if(type == 2){
      formData1.value = data
    }else if(type == 3){
      formData2.value = data
    }
  }
  
}

onMounted(() => {
  getConfig(1)
})
</script>