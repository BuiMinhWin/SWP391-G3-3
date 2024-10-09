import React from "react";
import { useField } from "formik";
import { useDropzone } from "react-dropzone";
import { Box, Typography, Button } from "@mui/material";

const FileInput = ({ label, ...props }) => {
  const [field, meta, helpers] = useField(props);

  const onDrop = (acceptedFiles) => {
    // Set the files in Formik state
    helpers.setValue(acceptedFiles);
  };

  const { getRootProps, getInputProps } = useDropzone({
    onDrop,
    accept: "application/pdf,image/*",
    "image/png": [], // Accept PNG files
    "image/jpeg": [], // Adjust accepted file types as needed
  });

  return (
    <Box>
      <Typography variant="h6" gutterBottom>
        {label}
      </Typography>
      <Box
        {...getRootProps()}
        sx={{
          border: "2px dashed #ccc",
          borderRadius: "5px",
          padding: "20px",
          textAlign: "center",
          cursor: "pointer",
        }}
      >
        <input {...getInputProps()} />
        <Typography variant="body2">
          Drag and drop some files here, or click to select files
        </Typography>
      </Box>
      {meta.touched && meta.error ? (
        <Typography color="error">{meta.error}</Typography>
      ) : null}
      {field.value && (
        <Box mt={2}>
          <Typography variant="body2">Files:</Typography>
          {Array.from(field.value).map((file, index) => (
            <Typography key={index} variant="body2">
              {file.name}
            </Typography>
          ))}
        </Box>
      )}
    </Box>
  );
};

export default FileInput;
