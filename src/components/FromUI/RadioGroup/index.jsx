import React from "react";
import {
  FormControl,
  FormControlLabel,
  FormLabel,
  Radio,
  RadioGroup,
} from "@mui/material";
import { useFormikContext } from "formik";

const RadioGroupWrapper = ({ service, serviceIds }) => {
  const { setFieldValue } = useFormikContext();

  const handleChange = (selectedValue) => {
    if (selectedValue === "Yes") {
      // Add service ID if "Yes" is selected
      setFieldValue("serviceIds", [...new Set([...serviceIds, service.id])]);
    } else {
      // Remove service ID if "No" is selected
      setFieldValue("serviceIds", serviceIds.filter((id) => id !== service.id));
    }
  };

  return (
    <FormControl>
      <FormLabel component="legend">{service.label}</FormLabel>
      <RadioGroup row>
        <FormControlLabel
          control={<Radio />}
          label="Yes"
          value="Yes"
          onChange={() => handleChange("Yes")}
        />
        <FormControlLabel
          control={<Radio />}
          label="No"
          value="No"
          onChange={() => handleChange("No")}
        />
      </RadioGroup>
    </FormControl>
  );
};

export default RadioGroupWrapper;
