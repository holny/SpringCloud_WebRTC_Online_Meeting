const getters = {
  hostToken: state => state.user.hostToken,
  hostRefreshToken: state => state.user.hostRefreshToken,
  hostUserName: state => state.user.hostUserName,
  hostAvatar: state => state.user.hostAvatar,
  hostStatus: state => state.user.hostStatus,
  hostAuthority: state => state.user.hostAuthority,
  hostRoles: state => state.user.hostRoles,
  hostInfo: state => state.user.hostInfo
}
export default getters
