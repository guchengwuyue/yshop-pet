import type { FormGroup } from '../types'

/** 线索表单默认配置（排除 id 及系统流转字段） */
export const cluesFormMock: FormGroup[] = [
  {
    id: 'group_basic',
    name: '基本信息',
    fields: [
      {
        id: 'field_name',
        fieldKey: 'name',
        label: '线索名称',
        fieldType: 'input',
        required: true,
        unique: false,
        placeholder: '请输入线索名称',
        defaultValue: '',
        min: 1,
        max: 255,
        enabled: true,
        system: true
      },
      {
        id: 'field_mobile',
        fieldKey: 'mobile',
        label: '客户手机',
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
        label: '客户电话',
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
        required: true,
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
        placeholder: '请选择客户标签',
        defaultValue: '',
        min: 1,
        max: 50,
        enabled: true,
        system: true
      },
      {
        id: 'field_nextTime',
        fieldKey: 'nextTime',
        label: '下次跟进时间',
        fieldType: 'datetime',
        required: false,
        unique: false,
        placeholder: '请选择下次跟进时间',
        defaultValue: '',
        min: null,
        max: null,
        enabled: true,
        system: true
      },
      {
        id: 'field_area',
        // 级联占位 key：运行时写回 province/city/area，非单独编辑 CluesVO.area
        fieldKey: 'area',
        label: '省市区',
        fieldType: 'cascader',
        dictType: 'area_cascader',
        required: false,
        unique: false,
        placeholder: '请选择省市区',
        defaultValue: '',
        min: null,
        max: null,
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
      }
    ]
  },
  {
    id: 'group_remark',
    name: '备注信息',
    fields: [
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
  }
]

/** 深拷贝默认配置，避免被引用修改 */
export function getDefaultCluesFormConfig(): FormGroup[] {
  return JSON.parse(JSON.stringify(cluesFormMock))
}
