const { Header, Footer, Sider, Content } = Layout;
const App = () => {
  const [selectedKey, setSelectedKey] = useState('1');

  return (
    <Layout>
      <Header><h1>Quiz管理系统</h1></Header>
      <Layout>
        <Sider>
          <Menu
            theme="dark"
            mode="inline"
            defaultSelectedKeys={['1']}
            onClick={({ key }) => setSelectedKey(key)}
            items={[
              { key: '1', icon: <UserOutlined />, label: '用户管理' },
              { key: '2', icon: <VideoCameraOutlined />, label: '题目管理' }
            ]}
          />
        </Sider>
        <Content>
          {selectedKey === '1' && (
            <>
              <SearchBar />
              <UserTable />
            </>
          )}
          {selectedKey === '2' && (
            <div>题目管理页面内容</div>
          )}
        </Content>