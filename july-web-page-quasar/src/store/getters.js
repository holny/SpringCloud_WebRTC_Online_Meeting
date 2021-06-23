const getters = {
  hostToken: state => state.user.hostToken,
  hostRefreshToken: state => state.user.hostRefreshToken,
  hostId: state => state.user.hostId,
  hostUserName: state => state.user.hostUserName,
  hostAvatar: state => state.user.hostAvatar,
  hostStatus: state => state.user.hostStatus,
  hostAuthority: state => state.user.hostAuthority,
  hostRoles: state => state.user.hostRoles
}
export default getters
