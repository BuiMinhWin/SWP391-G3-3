import React from 'react';
import { useField, useFormikContext } from 'formik';
import { TextField, Button } from '@mui/material';

const FileUploadWrapper = ({ name, multiple, ...otherProps }) => {
  const { setFieldValue } = useFormikContext();
  const [field, meta] = useField(name);

  const handleChange = (event) => {
    const files = event.currentTarget.files;
    const filesArray = multiple ? Array.from(files) : files[0];
    setFieldValue(name, filesArray);
  };

  const configFileUpload = {
    ...field,
    ...otherProps,
    type: 'file',
    onChange: handleChange,
  };

  if (meta && meta.touched && meta.error) {
    configFileUpload.error = true;
    configFileUpload.helperText = meta.error;
  }

  return (
    <>
      <Button variant="contained" component="label">
        Upload File
        <input hidden {...configFileUpload} multiple={multiple} />
      </Button>
      {meta && meta.touched && meta.error ? (
        <div style={{ color: 'red' }}>{meta.error}</div>
      ) : null}
    </>
  );
};

export default FileUploadWrapper;
