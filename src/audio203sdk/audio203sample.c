
/**
 * @file audio203sample.c
 * Very simple sample console application for Audio203 device USB API
 */
#include <stdio.h>
#include <windows.h>
#include "audio203api.h"
#include "usb_cmd.h"	// our protocol commands to communicate, file is shared with the device

// global flag indicates that user wants the application to exit (set from within CTRL-C handler)
int g_StopEvent = 0;

//===================================================
// handler for Win32 console, properly handle CTRL-C keycode - just sets global variable
BOOL WINAPI ConsoleControlHandler(DWORD dwCtrlType)
{
	switch (dwCtrlType) {
	case CTRL_BREAK_EVENT:
	case CTRL_C_EVENT:
	case CTRL_CLOSE_EVENT:
	case CTRL_LOGOFF_EVENT:
	case CTRL_SHUTDOWN_EVENT:
		printf("\nConsole Handler: CTRL-C pressed, stopping the application...\n");
		g_StopEvent = 1;
		return TRUE;
	}//switch
	return FALSE;
}

static void print_settings(usb_settings_t *pusb_settings)
{
	printf("contrast=%u; lighting=%u \n",
		pusb_settings->contrast,
		pusb_settings->lighting
		);
}

static void print_measure(usb_get_measure_t *get_measure, 
	usb_audio_result_t *get_measure2)
{
	printf("periods=%u, bad_periods=%u, expected_periods=%u \n",
		get_measure2->periods,
		get_measure2->bad_periods,
		get_measure2->expected_periods
		);
	printf("freq=%u, quality=%u, cl=%u, si=%u, hardness='%s' \n",
		get_measure2->freq,
		get_measure2->quality,
		get_measure2->cl,
		get_measure2->si,
		get_measure2->hardness_eng
		);
}

int main(int argc, char *argv[])
{
	int rc;
	int num_devices = 0;
	audio203_context_t pcontext = NULL;
	char device_name[256];
	int name_len;
	usb_get_status_t get_status;
	usb_settings_t usb_settings;
	usb_get_measure_t get_measure;
	usb_audio_result_t get_measure2;

	// how many devices connected?
	printf("=== How many devices connected?\n");
	rc = audio203_num_devices(&num_devices);
	if (rc != USB_ERR_OK) {
		printf("Error: could not retrieve number of devices: %s (%d)\n", audio203_errstr(rc), rc);
		goto lb_exit;
	}
	if (num_devices == 0) {
		printf("Error: no devices connected\n");
		goto lb_exit;
	}
	printf("Connected: %u devices\n", num_devices);

	printf("=== Open device\n");
	name_len = sizeof(device_name)-1;
	// open device, get context for future use, get device name
	rc = audio203_open(&pcontext, 0, device_name, name_len);
	if (rc != USB_ERR_OK) {
		printf("Error: no devices connected: %s (%d)\n", audio203_errstr(rc), rc);
		goto lb_exit;
	}
	printf("Device %s opened ok\n", device_name);

	printf("=== Check device status\n");
	Sleep(100);
	rc = audio203_get_status(pcontext, &get_status);
	if (rc != USB_ERR_OK) {
		printf("Error: could not retrieve device status: %s (%d)\n", audio203_errstr(rc), rc);
		goto lb_exit;
	}
	printf("Device version %u.%u.%u.%u, measure_on=%u \n", 
		get_status.version0, get_status.version1, get_status.version2, get_status.version3,
		get_status.measure_on);
	
	printf("=== Retrieve device settings\n");
	Sleep(100);
	rc = audio203_get_settings(pcontext, &usb_settings);
	if (rc != USB_ERR_OK) {
		printf("Error: could not retrieve device settings: %s (%d)\n", audio203_errstr(rc), rc);
		goto lb_exit;
	}
	print_settings(&usb_settings);

	printf("=== Get last measure\n");
	Sleep(100);
	memset(&get_measure, 0, sizeof(get_measure));
	memset(&get_measure2, 0, sizeof(get_measure2));
	rc = audio203_get_last_measure(pcontext, &get_measure, &get_measure2);
	if (rc != USB_ERR_OK) {
		printf("Error: could not get measure results: %s (%d)\n", audio203_errstr(rc), rc);
		goto lb_exit;
	}
	print_measure(&get_measure, &get_measure2);

lb_exit:
	printf("\n");
	if (pcontext) {
		rc = audio203_close(pcontext);
		if (rc == USB_ERR_OK) {
			printf("Device %s closed\n", device_name);
		}
	}
	// ok
	return 0;
}
