import { Button, Grid } from "@mui/material";
import React from 'react';
import PropTypes from 'prop-types';

FileHeader.propTypes = {
    file: PropTypes.instanceOf(File).isRequired, // Validate that file is an instance of File
    onDelete: PropTypes.func.isRequired, // Validate that onDelete is a function
  };
  
const FileHeader =({ file, onDelete }) => {
  return (
    <Grid container justify="space-between" alignItems="center">
      <Grid item>{file.name}</Grid>
      <Grid item>
        <Button size="small" onClick={() => onDelete(file)}>
          Delete
        </Button>
      </Grid>
    </Grid>
  );
}

export default FileHeader;