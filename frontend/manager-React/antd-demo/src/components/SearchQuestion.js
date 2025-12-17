//SearchQuestion.js的源代码:
import React, { useState } from 'react';
import { AudioOutlined } from '@ant-design/icons';
import { Input, Space, Button, Modal } from 'antd';
import AddQuestion from './AddQuestion';

const { Search } = Input;
const suffix = (
  <AudioOutlined
    style={{
      fontSize: 16,
      color: '#1890ff',
    }}
  />
);
const onSearch = (value) => console.log(value);
const App = () => {
  const [open, setOpen] = useState(false);

  const showModal = () => setOpen(true);
  const handleOk = () => setOpen(false);
  const handleCancel = () => setOpen(false);

  return (
    <>
      <Space direction="horizontal" style={{ margin: '16px 0 16px 16px' }}>
        <Search
          placeholder="请输入关键词"
          allowClear
          enterButton="查询题目"
          size="large"
          onSearch={onSearch}
        />
        <Button type="primary" onClick={showModal}>
          添加题目
        </Button>
        <Modal
          title="添加题目"
          open={open}
          onOk={handleOk}
          onCancel={handleCancel}
          footer={null}
          destroyOnClose
        >
          <AddQuestion />
        </Modal>
      </Space>
    </>
  );
};

export default App;