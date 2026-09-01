import { isCustomFieldKey } from './formConfig'
import request from '@/config/axios'

const EXT_PREFIX = 'crm_customer_ext_'

function storageKey(customerId: number | string): string {
  return `${EXT_PREFIX}${customerId}`
}

/** 从表单数据中挑出自定义字段 */
export function pickCustomFields(formData: Record<string, any>): Record<string, any> {
  const result: Record<string, any> = {}
  for (const key of Object.keys(formData || {})) {
    if (isCustomFieldKey(key)) {
      result[key] = formData[key]
    }
  }
  return result
}

/** 读取客户自定义字段 */
export async function getCustomerExt(
  customerId: number | string | undefined | null
): Promise<Record<string, any>> {
  if (customerId === undefined || customerId === null || customerId === '') return {}
  try {
    const res = await request.get({
      url: `/crm/form-data/get`,
      params: { module: 'customer', id: customerId }
    })
    if (!res?.formDataJson) return {}
    return JSON.parse(res.formDataJson)
  } catch {
    return {}
  }
}

/** 保存客户自定义字段 */
export async function saveCustomerExt(
  customerId: number | string | undefined | null,
  data: Record<string, any>
): Promise<void> {
  if (customerId === undefined || customerId === null || customerId === '') return
  const custom = pickCustomFields(data)
  await request.post({
    url: `/crm/form-data/create`,
    data: {
      formModule: 'customer',
      formDataId: customerId,
      formDataJson: JSON.stringify(custom)
    }
  })
}

/** 删除客户自定义字段本地缓存 */
export function removeCustomerExt(customerId: number | string | undefined | null): void {
  if (customerId === undefined || customerId === null || customerId === '') return
  localStorage.removeItem(storageKey(customerId))
}

/** 将自定义字段合并到单行（异步） */
export async function mergeCustomerExt<T extends { id?: number | string }>(
  row: T
): Promise<T & Record<string, any>> {
  if (row?.id == null) return { ...row }
  const ext = await getCustomerExt(row.id)
  return { ...row, ...ext }
}

/** 批量 merge（正确 await） */
export async function mergeCustomerExtList<T extends { id?: number | string }>(
  rows: T[]
): Promise<Array<T & Record<string, any>>> {
  return Promise.all((rows || []).map((row) => mergeCustomerExt(row)))
}
