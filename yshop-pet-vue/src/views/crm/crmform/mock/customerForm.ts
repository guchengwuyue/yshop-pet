import type { FormGroup } from '../types'

/** 客户表单默认配置（排除 id 及系统流转字段） */
export const customerFormMock: FormGroup[] = [
  {
    id: 'group_basic',
    name: '基本信息',
    fields: [
      {
        id: 'field_name',
        fieldKey: 'name',
        label: '客户名称',
        fieldType: 'input',
        required: true,
        unique: false,
        placeholder: '请输入客户名称',
        defaultValue: '',
        min: 1,
        max: 255,
        enabled: true,
        system: true
      },
      {
        id: 'field_mobile',
        fieldKey: 'mobile',
        label: '手机',
        fieldType: 'input',
        required: false,
        unique: true,
        placeholder: '请输入手机',
        defaultValue: '',
        min: 1,
        max: 20,
        enabled: true,
        system: true
      },
      {
        id: 'field_telephone',
        fieldKey: 'telephone',
        label: '电话',
        fieldType: 'input',
        required: false,
        unique: false,
        placeholder: '请输入电话',
        defaultValue: '',
        min: 1,
        max: 20,
        enabled: true,
        system: true
      },
      {
        id: 'field_level',
        fieldKey: 'level',
        label: '客户级别',
        fieldType: 'select',
        dictType: 'customer_level',
        required: false,
        unique: false,
        placeholder: '请选择客户级别',
        defaultValue: '3',
        min: null,
        max: null,
        enabled: true,
        system: true
      },
      {
        id: 'field_industry',
        fieldKey: 'industry',
        label: '客户行业',
        fieldType: 'select',
        dictType: 'customer_industry',
        required: false,
        unique: false,
        placeholder: '请选择客户行业',
        defaultValue: '9',
        min: null,
        max: null,
        enabled: true,
        system: true
      },
      {
        id: 'field_source',
        fieldKey: 'source',
        label: '客户来源',
        fieldType: 'select',
        dictType: 'customer_source',
        required: false,
        unique: false,
        placeholder: '请选择客户来源',
        defaultValue: '0',
        min: null,
        max: null,
        enabled: true,
        system: true
      },
      {
        id: 'field_tags',
        fieldKey: 'tags',
        label: '客户标签',
        fieldType: 'checkbox',
        required: false,
        unique: false,
        placeholder: '请选择标签',
        defaultValue: '',
        min: 1,
        max: 50,
        enabled: true,
        system: true
      },
      {
        id: 'field_addressStr',
        fieldKey: 'addressStr',
        label: '地理位置',
        fieldType: 'input',
        required: false,
        unique: false,
        placeholder: '请选择地址',
        defaultValue: '',
        min: 1,
        max: 255,
        enabled: true,
        system: true
      },
      {
        id: 'field_detailAddress',
        fieldKey: 'detailAddress',
        label: '详细地址',
        fieldType: 'input',
        required: false,
        unique: false,
        placeholder: '请输入详细地址',
        defaultValue: '',
        min: 1,
        max: 255,
        enabled: true,
        system: true
      },
      {
        id: 'field_remark',
        fieldKey: 'remark',
        label: '备注',
        fieldType: 'textarea',
        required: false,
        unique: false,
        placeholder: '请输入备注',
        defaultValue: '',
        min: 1,
        max: 255,
        enabled: true,
        system: true
      }
    ]
  },
  {
    id: 'group_ext',
    name: '扩展信息',
    fields: [
      {
        id: 'field_weixin',
        fieldKey: 'weixin',
        label: '微信',
        fieldType: 'input',
        required: false,
        unique: false,
        placeholder: '请输入微信',
        defaultValue: '',
        min: 1,
        max: 50,
        enabled: true,
        system: true
      },
      {
        id: 'field_qq',
        fieldKey: 'qq',
        label: 'QQ',
        fieldType: 'input',
        required: false,
        unique: false,
        placeholder: '请输入QQ',
        defaultValue: '',
        min: 1,
        max: 20,
        enabled: true,
        system: true
      }
    ]
  }
]

/** 深拷贝默认配置，避免被引用修改 */
export function getDefaultCustomerFormConfig(): FormGroup[] {
  return JSON.parse(JSON.stringify(customerFormMock))
}
