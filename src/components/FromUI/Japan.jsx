import React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import FlightIcon from '@mui/icons-material/Flight';

function JapanDialog({ open, onClose }) {
  return (
    <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
      <DialogTitle>
        <Typography variant="h6" sx={{ fontWeight: 600, textAlign: 'center' }}>
          Chọn Nơi Vận Chuyển
        </Typography>
      </DialogTitle>
      <Box sx={{ display: 'flex', justifyContent: 'space-around', p: 3 }}>
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            flexDirection: 'column',
            cursor: 'pointer',
            p: 2,
            borderRadius: 1,
            '&:hover': { bgcolor: '#f0f0f0' }
          }}
          onClick={() => console.log('Selected Trong nước')}
        >
          <LocalShippingIcon sx={{ fontSize: 40, mb: 1 }} />
          <Typography>Trong nước</Typography>
        </Box>
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            flexDirection: 'column',
            cursor: 'pointer',
            p: 2,
            borderRadius: 1,
            '&:hover': { bgcolor: '#f0f0f0' }
          }}
          onClick={() => console.log('Selected Nhật Bản')}
        >
          <FlightIcon sx={{ fontSize: 40, mb: 1 }} />
          <Typography>Nhật Bản</Typography>
        </Box>
      </Box>
    </Dialog>
  );
}

export default JapanDialog;