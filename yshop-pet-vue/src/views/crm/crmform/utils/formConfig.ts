import { CrmFormApi } from '@/api/crm/crmform'
import type { FormField, FormGroup } from '../types'


/** 可与 CustomerVO 直接映射（或经特殊处理映射）的系统字段 key */
export const CUSTOMER_SYSTEM_FIELD_KEYS = new Set([
  'name',
  'mobile',
  'telephone',
  'level',
  'industry',
  'source',
  'tags',
  'nextTime',
  /** 地图选址：UI 用 addressStr，写回 addressStr / detailAddress / lng / lat */
  'addressStr',
  'detailAddress',
  'remark',
  'weixin',
  'qq',
  'followStatus'
])

/** 地区级联字典占位，实际走 AreaApi，不走系统字典 */
export const AREA_CASCADER_DICT = 'area_cascader'

export function isCustomFieldKey(fieldKey: string): boolean {
  return fieldKey.startsWith('custom_')
}


export function isCustomerSystemFieldKey(fieldKey: string): boolean {
  return CUSTOMER_SYSTEM_FIELD_KEYS.has(fieldKey)
}

/** 加载线索表单配置 */
export async function loadCluesFormGroups(): Promise<FormGroup[]> {
  return CrmFormApi.getFormConfig('clues')
}

/** 加载客户表单配置 */
export async function loadCustomerFormGroups(): Promise<FormGroup[]> {
  return CrmFormApi.getFormConfig('customer')
}

/** 过滤出已启用的字段，空分组去掉 */
export function getEnabledGroups(groups: FormGroup[]): FormGroup[] {
  return groups
    .map((group) => ({
      ...group,
      fields: group.fields.filter((f) => f.enabled)
    }))
    .filter((group) => group.fields.length > 0)
}

/** 展平所有启用字段 */
export function getEnabledFields(groups: FormGroup[]): FormField[] {
  return getEnabledGroups(groups).flatMap((g) => g.fields)
}

/**
 * 对启用字段生成校验（含自定义字段）
 */
export function buildFormRules(fields: FormField[]): Record<string, any[]> {
  const rules: Record<string, any[]> = {}
  for (const field of fields) {
    if (!field.required) continue
    const trigger =
      field.fieldType === 'select' ||
      field.fieldType === 'radio' ||
      field.fieldType === 'checkbox' ||
      field.fieldType === 'cascader' ||
      field.fieldType === 'datetime'
        ? 'change'
        : 'blur'
    rules[field.fieldKey] = [
      {
        required: true,
        message: `${field.label}不能为空`,
        trigger
      }
    ]
  }
  return rules
}

/** @deprecated 使用 buildFormRules */
export const buildCluesFormRules = buildFormRules


/** 客户列表筛选项 */
export const CUSTOMER_LIST_SEARCHABLE_KEYS = new Set([
  'name',
  'mobile',
  'telephone',
  'level',
  'industry',
  'source',
  'weixin',
])

/** 客户列表动态列（tags 挂在 name 下，不单独成列） */
export const CUSTOMER_LIST_TABLE_KEYS = new Set([
  'name',
  'mobile',
  'telephone',
  'level',
  'industry',
  'source',
  'weixin',
])

/** 客户列表固定业务列 */
export const CUSTOMER_LIST_FIXED_EXTRA_KEYS = new Set([
  'ownUserName',
  'createTime',
  'followTime',
  'collectTime'
])

/**
 * 格式化字段展示文本（详情/列表单元格）
 */
export function formatFieldDisplay(field: FormField, value: any): string {
  if (value === undefined || value === null || value === '') return ''
  if (Array.isArray(value)) return value.filter((v) => v !== undefined && v !== null && v !== '').join(', ')
  if (field.fieldType === 'datetime') {
    const n = typeof value === 'number' ? value : Number(value)
    if (!Number.isNaN(n) && n > 0) {
      const d = new Date(n)
      if (!Number.isNaN(d.getTime())) {
        const pad = (x: number) => String(x).padStart(2, '0')
        return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
      }
    }
    return String(value)
  }
  return String(value)
}

/** 解析配置中的 defaultValue 为适合表单的值 */
export function parseFieldDefaultValue(field: FormField): any {
  const raw = field.defaultValue
  if (raw === undefined || raw === null || raw === '') {
    return undefined
  }
  if (field.fieldType === 'number') {
    const n = Number(raw)
    return Number.isNaN(n) ? undefined : n
  }
  if (
    field.fieldType === 'select' ||
    field.fieldType === 'radio' ||
    field.fieldKey === 'level' ||
    field.fieldKey === 'industry' ||
    field.fieldKey === 'source' ||
    field.fieldKey === 'followStatus'
  ) {
    const n = Number(raw)
    return Number.isNaN(n) ? raw : n
  }
  return raw
}


/** 根据配置构建客户系统字段默认值 */
export function buildCustomerSystemDefaults(fields: FormField[]): Record<string, any> {
  const defaults: Record<string, any> = {
    id: undefined,
    name: undefined,
    mobile: undefined,
    telephone: undefined,
    dealStatus: undefined,
    dealTime: undefined,
    isLock: undefined,
    level: 3,
    industry: 9,
    tags: '',
    source: 0,
    remark: undefined,
    userId: undefined,
    province: undefined,
    city: undefined,
    area: undefined,
    addressStr: undefined,
    detailAddress: undefined,
    lng: undefined,
    lat: undefined,
    nextTime: undefined,
    followTime: undefined,
    collectTime: undefined,
    weixin: undefined,
    qq: undefined,
    purchaseTotal: undefined,
    purchaseTimes: undefined,
    followStatus: 1,
    ownerUserId: undefined
  }
  for (const field of fields) {
    if (isCustomFieldKey(field.fieldKey)) continue
    if (!isCustomerSystemFieldKey(field.fieldKey)) continue
    if (field.fieldKey === 'addressStr') continue
    const parsed = parseFieldDefaultValue(field)
    if (parsed !== undefined) {
      defaults[field.fieldKey] = parsed
    }
  }
  return defaults
}

/** 提交前剥离自定义字段，仅保留后端可识别字段 */
export function stripCustomFields<T extends Record<string, any>>(data: T): T {
  const result = { ...data }
  for (const key of Object.keys(result)) {
    if (isCustomFieldKey(key)) {
      delete result[key]
    }
  }
  return result
}
