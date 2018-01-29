package models

// This file is auto-generated.
// Please contact avi-sdk@avinetworks.com for any change requests.

// TCPProxyProfile TCP proxy profile
// swagger:model TCPProxyProfile
type TCPProxyProfile struct {

	// Controls the our congestion window to send, normally it's 1 mss, If this option is turned on, we use 10 msses.
	AggressiveCongestionAvoidance bool `json:"aggressive_congestion_avoidance,omitempty"`

	// Dynamically pick the relevant parameters for connections.
	Automatic bool `json:"automatic,omitempty"`

	// Controls the congestion control algorithm we use. Enum options - CC_ALGO_NEW_RENO, CC_ALGO_CUBIC, CC_ALGO_HTCP.
	CcAlgo string `json:"cc_algo,omitempty"`

	// The duration for keepalive probes or session idle timeout. Max value is 3600 seconds, min is 5.  Set to 0 to allow infinite idle time. Allowed values are 5-3600. Special values are 0 - 'infinite'. Units(SEC).
	IDLEConnectionTimeout int32 `json:"idle_connection_timeout,omitempty"`

	// Controls the behavior of idle connections. Enum options - KEEP_ALIVE, CLOSE_IDLE.
	IDLEConnectionType string `json:"idle_connection_type,omitempty"`

	// A new SYN is accepted from the same 4-tuple even if there is already a connection in TIME_WAIT state.  This is equivalent of setting Time Wait Delay to 0.
	IgnoreTimeWait bool `json:"ignore_time_wait,omitempty"`

	// Controls the value of the Differentiated Services Code Point field inserted in the IP header.  This has two options   Set to a specific value, or Pass Through, which uses the incoming DSCP value. Allowed values are 0-63. Special values are MAX - 'Passthrough'.
	IPDscp int32 `json:"ip_dscp,omitempty"`

	// The number of attempts at retransmit before closing the connection. Allowed values are 3-8.
	MaxRetransmissions int32 `json:"max_retransmissions,omitempty"`

	// Maximum TCP segment size. Allowed values are 512-9000. Special values are 0 - 'Use Interface MTU'. Units(BYTES).
	MaxSegmentSize int32 `json:"max_segment_size,omitempty"`

	// The maximum number of attempts at retransmitting a SYN packet before giving up. Allowed values are 3-8.
	MaxSynRetransmissions int32 `json:"max_syn_retransmissions,omitempty"`

	// Consolidates small data packets to send clients fewer but larger packets.  Adversely affects real time protocols such as telnet or SSH.
	NaglesAlgorithm bool `json:"nagles_algorithm,omitempty"`

	// Size of the receive window. Allowed values are 32-65536. Units(KB).
	ReceiveWindow int32 `json:"receive_window,omitempty"`

	// The time (in millisec) to wait before closing a connection in the TIME_WAIT state. Allowed values are 500-2000. Special values are 0 - 'immediate'. Units(MILLISECONDS).
	TimeWaitDelay int32 `json:"time_wait_delay,omitempty"`

	// Use the interface MTU to calculate the TCP max segment size.
	UseInterfaceMtu bool `json:"use_interface_mtu,omitempty"`
}