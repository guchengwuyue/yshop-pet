/** 字段控件类型 */
export type FieldType =
  | 'input'
  | 'textarea'
  | 'select'
  | 'radio'
  | 'checkbox'
  | 'datetime'
  | 'cascader'
  | 'number'

/** 表单字段配置 */
export interface FormField {
  id: string
  /**
   * 对应业务字段 key；自定义字段可用 custom_xxx。
   * 特殊：
   * - fieldKey === 'area' 且 fieldType === 'cascader'：线索省市区级联，写回 province/city/area
   * - fieldKey === 'addressStr'：客户地图选址，写回 addressStr/detailAddress/lng/lat
   */
  fieldKey: string
  /** 字段名称 */
  label: string
  fieldType: FieldType
  /**
   * 关联字典 type（与 DICT_TYPE 一致，如 customer_level）。
   * area_cascader 为占位，实际走 AreaApi 地区树，不读系统字典。
   */
  dictType?: string
  required: boolean
  unique: boolean
  placeholder?: string
  defaultValue?: string
  min?: number | null
  max?: number | null
  /** 状态：启用 */
  enabled: boolean
  /** 系统字段不可删除 */
  system: boolean
}

/** 表单分组 */
export interface FormGroup {
  id: string
  name: string
  fields: FormField[]
}

/** 业务模块 */
export type FormModule = 'clues' | 'customer'

/** 字段类型选项 */
export const FIELD_TYPE_OPTIONS: { label: string; value: FieldType }[] = [
  { label: '文本框', value: 'input' },
  { label: '文本域', value: 'textarea' },
  { label: '下拉单选', value: 'select' },
  { label: '单选按钮', value: 'radio' },
  { label: '复选按钮组', value: 'checkbox' },
  { label: '日期时间控件', value: 'datetime' },
  { label: '级联选择', value: 'cascader' },
  { label: '数字', value: 'number' }
]

/** 关联字典选项（value 与 DICT_TYPE / 系统字典 type 一致；area_cascader 走 AreaApi） */
export const DICT_TYPE_OPTIONS: { label: string; value: string }[] = [
  { label: '客户级别', value: 'customer_level' },
  { label: '客户行业', value: 'customer_industry' },
  { label: '客户来源', value: 'customer_source' },
  { label: '跟进状态', value: 'follow_status' },
  { label: '地区级联', value: 'area_cascader' }
]

/** 支持边界值的字段类型 */
export const BOUNDARY_SUPPORTED_TYPES: FieldType[] = ['input', 'textarea', 'number', 'checkbox']

/** 支持关联字典的字段类型 */
export const DICT_SUPPORTED_TYPES: FieldType[] = ['select', 'radio', 'checkbox', 'cascader']

export function supportsBoundary(type: FieldType): boolean {
  return BOUNDARY_SUPPORTED_TYPES.includes(type)
}

export function supportsDict(type: FieldType): boolean {
  return DICT_SUPPORTED_TYPES.includes(type)
}
