
/**
 * @file usb_cmd.h
 * USB protocol commands and data structures
 * shared between PC host and device
 */
#ifndef __USB_CMD_H__
#define __USB_CMD_H__

#ifdef WIN32
#pragma pack(1)
/// data types definition for PC host
typedef unsigned char  U8;
typedef unsigned short U16;
typedef unsigned int   U32;
typedef struct {
	U16 n;	//number
	U8 point;	// position of decimal point, starting from end
} F24;	//float
#endif

#define ARR_SIZE(a) (sizeof(a)/sizeof(a[0]))

/// list of commands
#define USB_CMD_NO_COMMAND		0
#define USB_CMD_GET_STATUS		1
#define USB_CMD_GET_TIME		2
#define USB_CMD_SET_TIME		3
#define USB_CMD_GET_SETTINGS	4
#define USB_CMD_SET_SETTINGS	5
#define USB_CMD_CLEAR_LOG		6
#define USB_CMD_SET_MEASURE		7
#define USB_CMD_START_MEASURE	8
#define USB_CMD_GET_MEASURE		9
#define USB_CMD_STOP_MEASURE	10
#define USB_CMD_GET_RECORDS		11
#define USB_CMD_CLEAR_RECORDS	12
#define USB_CMD_GET_LAST_MEASURE	13

#define USB_MEASURE_STATUS_PROGRESS 1
#define USB_MEASURE_STATUS_DONE	2

/// All fields in BCD format!
typedef struct {
	U8 second;	//[0..59]
	U8 minute;	//[0..59]
	U8 hour;	//[0..23]
	U8 day;		//[1.. 7] day of the week
	U8 date;	//[1..31] day of the month
	U8 month;	//[1..12]
	U8 year;	//[0..99]
} usb_rtc_time_t;

/// CMD_GET_STATUS
typedef struct {
	U8 cmd;
	U8 version0;
	U8 version1;
	U8 version2;
	U8 version3;
	U8 measure_on;
	U8 reserved[2];
} usb_get_status_t;

/// CMD_GET_SETTINGS
typedef struct {
	U8 cmd;
	U8 size;	// size of settings structure (in following packets)
	U8 reserved[6];
} usb_get_settings_t;

/// device settings structure, copied from audio203 project
// 25 bytes
typedef struct {
// measure settings, 12 bytes
	U8 freq_range;				// 0..15
	U16 delay_before_measure;	// millis, 0..9999
	F24 form_coef;				// form coefficient, *0.001
	U32 measure_time;	// user defined measure time, ms
	U8 instrument;	// [0,1,2] instrument type: K, B, G
	U8 instrument_subtype;	//[0,1] instrument subtype for K: [24A, 63C]
	// young module: 13 bytes
	U8 young_use_mdhd;	// [0,1] whether to use density or mDhd for input
	U8 young_use_m100;	// [0,1] whether to use 100g for mass measurement
	F24 density;	// for young module calc
	U16 young_m;	// mass, g
	U16 young_D;	//diameter, mm
	U16 young_h;	//?, mm
	U16 young_d;	//diameter, mm
} usb_measure_settings_t;

// 33 bytes
typedef struct {
	// common settings, 8 bytes
	U8 lighting;	// current lighting value [0..255]
	U8 contrast;	// current contrast value [0..255]
	U8 results_mode;	// [0..4] - freq, Cl, SI, hardness1, hardness2
	U8 measure_on;	// [0,1] whether measure is on or off (Shift key)
	U8 sensitivity;	// [0..255] audio detection sensitivity
	U16 delay_after_measure;	// millis
	U8 use_periods;	// [0,1] whether to use periods or millis for measure 
	// 25 bytes
	usb_measure_settings_t m;
} usb_settings_t;

/// USB_CMD_SET_MEASURE
typedef struct {
	U8 cmd;
	U8 freq_range;				// 0..15
	U16 delay_before_measure;	// millis, 0..9999
	U32 measure_time;	// user defined measure time, ms
} usb_set_measure_t;

/// USB_CMD_GET_MEASURE
typedef struct {
	U8 cmd;
	U8 status;
	U8 size;	// audio_results size
	U8 reserved[5];
} usb_get_measure_t;
/// copied from storage.h::audio_result_t
typedef struct {
	U16 periods;	// number of periods detected
	U16 bad_periods;	// number of bad periods detected
	U16 expected_periods;	// number of periods expected (ideal)
	U16 freq;		// signal frequency
	U16 quality;	// measure quality, percents 0..100
	U16 cl;	// audio velocity
	U8 si;	//sound index
	char hardness_rus[6];	// hardness type, character string
	char hardness_eng[6];	// hardness type, character string
	F24 e;	// young module
} usb_audio_result_t;

/// USB_CMD_GET_RECORDS
typedef struct {
	U8 cmd;
	U8 num_records;
	U8 record_size;
	U8 reserved[5];
} usb_get_records_t;
typedef struct {
	usb_rtc_time_t time;				// 7 bytes
	usb_measure_settings_t settings;	// 25 bytes
	usb_audio_result_t result;			// 28 bytes
} usb_storage_record_t;

#ifdef WIN32
#pragma pack()
#endif

#endif	// __USB_CMD_H__
