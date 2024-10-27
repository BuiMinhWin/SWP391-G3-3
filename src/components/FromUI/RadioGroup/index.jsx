import React, { useEffect } from "react";
import {
  FormControl,
  FormControlLabel,
  FormLabel,
  Radio,
  RadioGroup,
} from "@mui/material";
import { useField, useFormikContext } from "formik";

const RadioGroupWrapper = ({ name, legend, options, ...otherProps }) => {
  const { setFieldValue } = useFormikContext(); // Get Formik context
  const [field] = useField(name); // Field from Formik

  // Auto-set the value to "Yes" when the component mounts
  useEffect(() => {
    if (!field.value) {
      setFieldValue(name, "Yes");
    }
  }, [name, setFieldValue, field.value]);

  const handleChange = (evt) => {
    console.log(`Radio changed: ${evt.target.value}`);
    setFieldValue(name, evt.target.value); // Update Formik state on change
  };

  return (
    <FormControl>
      <FormLabel component="legend">{legend}</FormLabel>
      <RadioGroup
        {...field}
        value={field.value} // Bind value to Formik field
        onChange={handleChange}
        row // Display radios in a row
      >
        {options.map((option) => (
          <FormControlLabel
            key={option.value}
            value={option.value}
            control={<Radio />}
            label={option.label}
            sx={{ color: "black" }} // Set label color to black
          />
        ))}
      </RadioGroup>
    </FormControl>
  );
};

export default RadioGroupWrapper;
