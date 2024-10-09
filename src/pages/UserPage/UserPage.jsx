import * as React from 'react';
DataGr
import { TextField } from '@mui/material';

const rows = [
  { id: 1, name: 'John Doe', age: 35 },
  { id: 2, name: 'Jane Smith', age: 42 },
  { id: 3, name: 'Alice Johnson', age: 28 },
];

const UserPage = () => {
  const [data, setData] = React.useState(rows);

  const handleInputChange = (id, field, value) => {
    setData(prevData =>
      prevData.map(row => (row.id === id ? { ...row, [field]: value } : row))
    );
  };

  const columns = [
    { field: 'id', headerName: 'ID', width: 90 },
    {
      field: 'name',
      headerName: 'Name',
      width: 150,
      editable: true,
      renderCell: (params) => (
        <TextField
          value={params.value}
          onChange={(e) => handleInputChange(params.row.id, 'name', e.target.value)}
        />
      ),
    },
    {
      field: 'age',
      headerName: 'Age',
      width: 110,
      renderCell: (params) => (
        <TextField
          type="number"
          value={params.value}
          onChange={(e) => handleInputChange(params.row.id, 'age', e.target.value)}
        />
      ),
    },
  ];

  return (
    <div style={{ height: 400, width: '100%' }}>
      <DataGrid
        rows={data}
        columns={columns}
        pageSize={5}
        rowsPerPageOptions={[5]}
        disableSelectionOnClick
      />
    </div>
  );
};

export default UserPage;
