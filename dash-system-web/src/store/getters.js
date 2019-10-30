const getters = {
  sidebar: state => state.app.sidebar,
  language: state => state.app.language,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  userId: state => state.user.userId,
  name: state => state.user.name,
  userName: state => state.user.userName,
  introduction: state => state.user.introduction,
  roles: state => state.user.roles,
  menuList: state => state.user.menuList,
  permission_routes: state => state.permission.routes,
  hasDynamicRoutes: state => state.permission.hasDynamicRoutes,
  addRoutes: state => state.permission.addRoutes,
  errorLogs: state => state.errorLog.logs
}
export default getters
