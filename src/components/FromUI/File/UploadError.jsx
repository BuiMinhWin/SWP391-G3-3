import React from "react";
import { FileHeader } from "./FileHeader";
import {
  withStyle,
  createStyles,
  LinearProgress,
  Typography,
} from "@mui/material";

const ErrorLinearProgress = withStyle((theme) =>
  createStyles({
    bar: {
      backgroundColor: theme.palette.error.main,
    },
  })
)(LinearProgress);

const UploadError = ({ file, onDelete, errors }) => {
  return (
    <React.Fragment>
      <FileHeader file={file} onDelete={onDelete} />
      <ErrorLinearProgress variant="determinate" value={100} />
      {errors.map((error) => (
        <div key={error.code}>
          <Typography color="error">{error.message}</Typography>
        </div>
      ))}
    </React.Fragment>
  );
}
export default UploadError;