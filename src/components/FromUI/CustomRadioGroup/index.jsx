import React from 'react';
import {
  FormControl,
  FormControlLabel,
  Radio,
  RadioGroup,
  Grid,
  Typography,
  Box,
  Tooltip,
  Divider,
} from '@mui/material';
import { useFormikContext, useField } from 'formik';
import { HelpOutline } from '@mui/icons-material';

const CustomRadioGroup = ({ name, options }) => {
  const { setFieldValue } = useFormikContext();
  const [field] = useField(name);

  const handleChange = (event) => {
    setFieldValue(name, event.target.value);
  };

  return (
    <FormControl component="fieldset" fullWidth>
      <RadioGroup
        row
        name={name}
        value={field.value}
        onChange={handleChange}
        sx={{ justifyContent: 'space-between' }}
      >
        {options.map((option) => (
          <Grid
            key={option.value}
            item
            xs={5.5}
            sx={{
              border: '1px solid #ccc',
              borderRadius: 2,
              padding: 2,
              display: 'flex',
              alignItems: 'center',
              transition: 'border-color 0.3s, color 0.3s',
              ...(field.value === option.value && {
                borderColor: '#415A77',
                '& .MuiTypography-root': { color: '#171B36' },
              }),
              '&:hover': {
                borderColor: '#171B36',
                '& .MuiTypography-root': { color: '#171B36' },
              },
            }}
          >
            <FormControlLabel
              value={option.value}
              control={<Radio sx={{ mr: 2 }} />}
              label={
                <Box sx={{ display: 'flex', flexDirection: 'column' }}>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                      {option.label}
                    </Typography>
                    <Tooltip title={option.helpText} arrow placement="top">
                      <HelpOutline
                        fontSize="small"
                        sx={{ ml: 1, cursor: 'pointer' }}
                      />
                    </Tooltip>
                  </Box>
                  <Typography variant="body2" color="textSecondary">
                    {option.description}
                  </Typography>
                </Box>
              }
              sx={{ flex: 1 }}
            />
            <Divider orientation="vertical" flexItem sx={{ mx: 2 }} />
            {option.icon && <Box>{option.icon}</Box>}
          </Grid>
        ))}
      </RadioGroup>
    </FormControl>
  );
};

export default CustomRadioGroup;
