<template>
  <div class="app-title">
    <div class="title-font">
      <h1 class="text-jumbo text-ginormous">
        付款成功！
      </h1>
    </div>
  </div>

  <!-- <div class="errPage-container">
    <div >
      <div>
        <h1 class="text-jumbo text-ginormous">
          付款成功！
        </h1>
      </div>
    </div>
  </div> -->
</template>

<script>
// import errGif from '@/assets/401_images/401.gif'

export default {
  name: 'Page401',
  data() {
    return {
      dialogVisible: false
    }
  },
  methods: {

  }
}
</script>

<style lang="scss" scoped>
.app-title{
  width: 100%;
  padding-top: 70px;
  display: table;
  .title-font{
    width: 100%;
    font-size: 32px;
    text-align: center;
    display: table-cell;
    vertical-align: middle;
  }
  .text-jumbo {
    font-size: 60px;
    font-weight: 700;
    color: #484848;
  }
}
  .errPage-container {
    width: 800px;
    max-width: 100%;
    margin: 100px auto;
    .pan-back-btn {
      background: #008489;
      color: #fff;
      border: none!important;
    }
    .pan-gif {
      margin: 0 auto;
      display: block;
    }
    .pan-img {
      display: block;
      margin: 0 auto;
      width: 100%;
    }
    .text-jumbo {
      font-size: 60px;
      font-weight: 700;
      color: #484848;
    }
    .list-unstyled {
      font-size: 14px;
      li {
        padding-bottom: 5px;
      }
      a {
        color: #008489;
        text-decoration: none;
        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
</style>
