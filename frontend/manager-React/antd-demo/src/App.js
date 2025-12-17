// // import logo from './logo.svg';
// // import './App.css';
// // import { Button } from 'antd';

// // function App() {
// //   return (
// //     <div className="App">
// //       <header className="App-header">
// //         {/* <img src={logo} className="App-logo" alt="logo" />
// //         <p>
// //           Edit <code>src/App.js</code> and save to reload.
// //         </p>
// //         <a
// //           className="App-link"
// //           href="https://reactjs.org"
// //           target="_blank"
// //           rel="noopener noreferrer"
// //         >
// //           Learn React
// //         </a> */}
// //         <Button type="primary">Button</Button>
// //       </header>
// //     </div>
// //   );
// // }

// // export default App;

// // -------------

// import { UploadOutlined, UserOutlined, VideoCameraOutlined } from '@ant-design/icons';
// import { Layout , Menu } from 'antd';
// import React, { useState } from 'react';
// import SearchUser from './components/SearchUser';
// import UserTable from './components/UserTable';
// import SearchQuestion from './components/SearchQuestion';
// import QuestionTable from './components/QuestionTable'
// const { Header, Footer, Sider, Content } = Layout;
// const App = () => {
//   const [selectedKey, setSelectedKey] = useState('1');

//   return (
//     <>
//       <Layout>
//       {/* <Header>Header</Header> */}
//       <Header><h1 style={{color: 'white'}}>Quiz管理系统</h1></Header>
//       <Layout>
//         {/* <Sider>Sider</Sider> */}
//         <Sider trigger={null}>
//         {/* <div className="logo" /> */}
//         <Menu
//           theme="dark"
//           mode="inline"
//           defaultSelectedKeys={['1']}
//           onClick={({ key }) => setSelectedKey(key)}
//           items={[
//             {
//               key: '1',
//               icon: <UserOutlined />,
//               label: '用户管理',
//             },
//             {
//               key: '2',
//               icon: <VideoCameraOutlined />,
//               label: '题目管理',
//             },
//           ]}
//         />
//         </Sider>
//         {/* <Content>Content</Content> */}
//         <Content>
//           {/* <SearchUser/>
//           <UserTable/> */}
//           {selectedKey === '1' && (
//             <>
//               <SearchUser />
//               <UserTable />
//             </>
//           )}
//           {selectedKey === '2' && (
//             // <div>题目管理页面内容</div>

//             <>
//             <SearchQuestion />
//             <QuestionTable />
//             </>
            
//           )}
//         </Content>
//       </Layout>
//       {/* <Footer>Footer</Footer> */}
//       <Footer style={{
//         textAlign: 'center',
//       }}
//       >Quiz管理系统 ©2025 Created by Joe</Footer>
//     </Layout>
//   </>
//   );
// }
// export default App;

// -------------

// import { UploadOutlined, UserOutlined, VideoCameraOutlined } from '@ant-design/icons';
import { UserOutlined, VideoCameraOutlined } from '@ant-design/icons';
import { Layout , Menu } from 'antd';
import React, { useState } from 'react';
// import SearchUser from './components/SearchUser';
// import UserTable from './components/UserTable';
// import SearchQuestion from './components/SearchQuestion';
// import QuestionTable from './components/QuestionTable'
import UsersManager from './components/UsersManager';
import QuestionsManager from './components/QuestionsManager';
const { Header, Footer, Sider, Content } = Layout;
const App = () => {
  const [selectedKey, setSelectedKey] = useState('1');

  return (
    <>
      <Layout>
        <Header><h1 style={{color: 'white', margin: 0}}>Quiz管理系统</h1></Header>
        <Layout>
          <Sider trigger={null} width={220}>
            <Menu
              theme="dark"
              mode="inline"
              selectedKeys={[selectedKey]}
              onClick={({ key }) => setSelectedKey(key)}
              items={[
                { key: '1', icon: <UserOutlined />, label: '用户管理' },
                { key: '2', icon: <VideoCameraOutlined />, label: '题目管理' },
              ]}
            />
          </Sider>
          <Content style={{ padding: 16, background: '#fff', minHeight: 'calc(100vh - 134px)' }}>
            {selectedKey === '1' && <UsersManager />}
            {selectedKey === '2' && <QuestionsManager />}
          </Content>
        </Layout>
        <Footer style={{ textAlign: 'center' }}>Quiz管理系统 ©2025 Created by Joe</Footer>
      </Layout>
    </>
  );
}
export default App;