
/**
 * @file audio203api.h
 * Audio203v2 USB Driver API definition
 */
#ifndef __AUDIO203_API_H__
#define __AUDIO203_API_H__

#include "usb_cmd.h"	// our protocol commands to communicate, file is shared with the device

#ifdef __cplusplus
extern "C" {
#endif

/// audio203api context
typedef void * audio203_context_t;

#pragma pack(1)
/// buffer to exchange commands with the device, always use 8 bytes only
typedef struct {
	unsigned char data[8];
} usb_buffer_t;
#pragma pack()

// error codes returned by all functions
#define USB_ERR_OK	(0)
#define USB_ERR_TIMEOUT	(-1)	//timeout
#define USB_ERR_OPERATION	(-2)	//operation failed
#define USB_ERR_SIZE	(-3)	//transaction size less than requested
#define USB_ERR_PARAMS	(-4)	//wrong parameters
#define USB_ERR_RESPONSE	(-5)	//wrong response
#define USB_ERR_MEMORY	(-6)	//could not alloc mem
#define USB_ERR_FAIL	(-7)	//device could not perform requested operation

/// @param err_code - error code to describe
/// @return string description of the error code
char *audio203_errstr(int err_code);

/// get version of the library
int audio203_get_version(int *pmajor, int *pminor, int *prevision, int *pbuild);

/// get number of devices
/// @param pnum_devices - [OUT] pointer to number of devices
int audio203_num_devices(int *pnum_devices);

/// open device
/// @param ppcontext - [OUT] pointer to var to receive initialized device context
/// @param device_num - ordinal number of device [0..n]
/// @param pdevice_name - [OUT] pointer to device name
/// @param name_len - length of device_name variable
int audio203_open(audio203_context_t *ppcontext, int device_num, char *pdevice_name, int name_len);

/// close device, free context
int audio203_close(audio203_context_t pcontext);

/// get device status
/// @param pget_status - pointer to structure to receive status info
int audio203_get_status(audio203_context_t pcontext, usb_get_status_t *pget_status);

/// get device settings
/// @param psettings - pointer to structure to receive device settings
int audio203_get_settings(audio203_context_t pcontext, usb_settings_t *psettings);

/// set measure parameters
/// @param pset_measure - pointer to structure with measure parameters to pass into device
/// @param pretcode - [OUT] device return code [0=ok,1=error]
int audio203_set_measure(audio203_context_t pcontext, usb_set_measure_t *pset_measure, U8 *pretcode);

/// start measure
/// @param pretcode - [OUT] device return code [0=ok,1=error]
int audio203_start_measure(audio203_context_t pcontext, U8 *pretcode);

/// get measure results
/// @param pget_measure - pointer to struct to receive measure results1
/// @param pget_measure2 - pointer to struct to receive measure results2
/// @param pretcode - [OUT] device return code [0=not started, 1=in progress, 2=done]
int audio203_get_measure(audio203_context_t pcontext, usb_get_measure_t *pget_measure, 
	usb_audio_result_t *pget_measure2);

/// get storage records (saved measures)
/// @param pget_records - pointer to struct to receive records info
/// @param records [OUT] - internally allocated array of records, free it after use
int audio203_get_records(audio203_context_t pcontext, usb_get_records_t *pget_records, 
	usb_storage_record_t* *pprecords);

/// clear records storage on the device
int audio203_clear_records(audio203_context_t pcontext);

/// stop measure
int audio203_stop_measure(audio203_context_t pcontext);

/// get last measure results
/// @param pget_measure - pointer to struct to receive measure results1
/// @param pget_measure2 - pointer to struct to receive measure results2
/// @param pretcode - [OUT] device return code [0=not started, 1=in progress, 2=done]
int audio203_get_last_measure(audio203_context_t pcontext, usb_get_measure_t *pget_measure, 
	usb_audio_result_t *pget_measure2);

#ifdef __cplusplus
}
#endif

#endif //__AUDIO203_API_H__
