<template>
  <!-- <div>
     <h1>{{message}}</h1>
      //引入elementview组件；
     <element-view>
     </element-view>
  </div> -->
  <div id="app">
    <!-- <user-view></user-view> -->
    <!-- <question-vew></question-vew> -->
     <router-view></router-view>
    


    <!-- <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>
    </nav>
    <router-view/> -->
  </div>
</template>

<script>
  // import QuestionVew from './views/element/QuestionVew.vue';
  // import UserView from './views/element/UserView.vue';

  export default {
    // components: {QuestionVew },
    data() {
      return {
        message: "hello, vue",
      };
    },

    methods: {
      
    }
  };

    // //import element view组件;
    // import ElementView from './views/element/ElementView.vue'
    // export default{
    //     components: { ElementView },
    //     data(){
    //         return{
    //             message:"hello vue"
    //         }
    //     },
    //     methods:{
            
    //     }
    // }
</script>

<style>



/* h1{
  color:red;
} */

/* #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
} */
</style>
