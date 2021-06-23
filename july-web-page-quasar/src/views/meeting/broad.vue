<template>
    <!--我这个需求是有消息时才会让这个功能显示，所以使用了v-if-->
    <div class="bs-sysMsg" v-if="systemMsg.length > 0">
      <i class="el-alert__icon el-icon-warning"></i>
      <div class="msg__content">
        <el-carousel height="20px" direction="vertical" indicator-position="none" :autoplay="true">
          <el-carousel-item v-for="item in systemMsg" :key="item.id">
            <a href="javascript:void(0)" class="item">{{item.title}}</a>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>

</template>

<script>

export default {
  name: 'chat',
  data () {
    return {
      // 滚屏消息
      systemMsg: [
        {id: 1, title: '入主白宫近10日 拜登做了10件大事'},
        {id: 2, title: '全民带货？小红书外链淘宝权限将大范围开放'},
        {id: 3, title: '贾跃亭FF将在纳斯达克上市 股票代码为FFIE'}
      ],
    }
  },
  methods: {

  },
  mounted () {
  }
}

</script>

<style lang="less" scoped>
.message {
  height: 350px;
}
.ivu-card-body {
  padding:5px;
}
.ivu-modal-body{
  padding: 0px 16px 16px  16px;
}
.chat-message-body {
  background-color:#F8F8F6;
  width:545px;
  height: 350px;
  overflow: auto;
}
.message-card {
  margin:5px;
}
.message-row-left {
  display: flex;
  flex-direction:row;
}
.message-row-right {
  display: flex;
  flex-direction:row-reverse;
}
.message-content {
  margin:-5px 5px 5px 5px;
  display: flex;
  flex-direction:column;
}
.message-body {
  border:1px solid #D9DAD9;
  padding:5px;
  border-radius:3px;
  background-color:#FFF;
}
.message-time {
  margin:0 5px;
  font-size:5px;
  color:#D9DAD9;
}
.footer-btn {
  float:right;
  margin-bottom: 5px;
}
.spin-icon-load {
  animation:ani-spin 1s linear infinite;
}
@keyframes ani-spin{
  form{transform: rotate(0deg);}
  50% {transform: rotate(180deg);}
  to  {transform: rotate(360deg);}
}
</style>
