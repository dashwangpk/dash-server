import generate from '@/utils/apiGenerater'

import sysAppMenu from './sys/appMenu'
import sysUser from './sys/user'
import sysRole from './sys/role'
// cfg模块
import payConfig from './cfg/paycfg'
import cfgAa10 from './cfg/aa10'
// 对账
import payPatternPlatform from './pay/paypattern-platform'
import payRefundTrade from './pay/pay-refund-trade'
import payBillAccount from './pay/bill-account'
import payStatistic from './pay/statistic'

import cfgApp from './cfg/app'
import cfgHospital from './cfg/hospital'
import cfgScene from './cfg/scene'
import cfgVpaymode from './cfg/vpaymode'
import cfgCollectfee from './cfg/collectfee'
import cfgChannel from './cfg/channel'

import payClientPayOrder from './client/pay-order'
import payPayOrder from './pay/pay-order'

const api = {
  // sys模块
  sysAppMenu,
  sysUser,
  sysRole,
  // cfgs模块
  payConfig,
  cfgAa10,
  cfgApp,
  cfgHospital,
  cfgScene,
  cfgVpaymode,
  cfgCollectfee,
  payPatternPlatform,
  payRefundTrade,
  cfgChannel,
  // pay模块
  payStatistic,
  payBillAccount,
  payClientPayOrder,
  payPayOrder
}

export default generate(api)
