import React from "react";
import { Formik, Form, Field } from "formik";
import * as Yup from "yup";
import { TextField, Button, Typography, Box } from "@mui/material";
import Rating from "@mui/material/Rating";
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import axios from "axios";


const REST_API_FEEDBACK = "http://koideliverysystem.id.vn:8080/api/feedbacks"

// Custom styled rating component
const StyledRating = (props) => {
  return (
    <Rating
      {...props}
      icon={<FavoriteIcon fontSize="inherit" style={{ color: "red" }} />}
      emptyIcon={<FavoriteBorderIcon fontSize="inherit" />}
    />
  );
};

const FeedbackForm = ({ orderId }) => {
  const INITIAL_FORM_STATE = {
    feedbackId: "", 
    orderId: orderId,
    rating: 0,
    comment: "",
    createdAt: new Date().toISOString(),
    status: 0,
  };

  const FORM_VALIDATION = Yup.object().shape({
    comment: Yup.string()
      .max(500, "Feedback must be 500 characters or less")
      .required("Feedback is required"),
    rating: Yup.number()
      .min(1, "Rating is required")
      .max(5, "Rating cannot be more than 5")
      .required("Rating is required"),
  });

  const handleSubmit = async (values, { setSubmitting }) => {
    try {
      const response = await axios.post(`${REST_API_FEEDBACK}/create`, values);
      console.log("Feedback submitted successfully:", response.data);
    } catch (error) {
      console.error("Error submitting feedback:", error);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Formik
      initialValues={INITIAL_FORM_STATE}
      validationSchema={FORM_VALIDATION}
      onSubmit={handleSubmit}
    >
      {({ values, setFieldValue }) => (
        <Form>
          <Typography variant="h6">Feedback</Typography>
          <Typography component="legend">Đánh giá</Typography>
          <Box marginBottom={2}>
            <StyledRating
              name="rating"
              value={values.rating}
              onChange={(event, newValue) => {
                setFieldValue("rating", newValue);
              }}
              max={5} // Setting the max rating value to 5
            />
          </Box>
          <Field
            as={TextField}
            name="comment"
            label="Comment"
            multiline
            rows={4}
            variant="outlined"
            fullWidth
            margin="normal"
          />
          <Button type="submit" variant="contained" color="primary">
            Submit Feedback
          </Button>
        </Form>
      )}
    </Formik>
  );
};

export default FeedbackForm;
