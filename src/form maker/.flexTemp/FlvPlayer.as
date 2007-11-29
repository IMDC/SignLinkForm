package 
{
	import flash.display.Sprite;
	import flash.net.NetConnection;
	import flash.net.NetStream;
	import flash.media.Video;
	import flash.text.TextFieldAutoSize;
	import flash.text.TextField;
	import flash.events.Event;
	import flash.events.TimerEvent;
	import flash.events.NetStatusEvent;
	import flash.utils.Timer;
	import flash.events.AsyncErrorEvent;

	public class FlvPlayer extends Sprite
	{
		private var video		:Video;
		private var stream		:NetStream;
		private var connection	:NetConnection;
		private var timer		:Timer;
		private var duration	:Number;
		private var flvUrl		:String;
		private var autoPlay	:Boolean;
		private var playing		:Boolean;

		public function FlvPlayer(url:String,auto:Boolean=false):void
		{
			autoPlay	= auto;
			playing		= auto;
			duration	= 0;
			flvUrl		= url;
			connection	= new NetConnection;
			connection.addEventListener(NetStatusEvent.NET_STATUS,onNetStatus);
			connection.connect(null);
		}
		
		private function onNetStatus(event:NetStatusEvent):void
		{
			switch (event.info.code)
			{
				case "NetConnection.Connect.Success" :
					connectStream();
					break;
				case "NetStream.Play.StreamNotFound" :
					trace("Unable to locate video: " + flvUrl);
					break;
				case "NetStream.Play.Start" :
					stream.seek(0);					
					if (!autoPlay){ this.pause(); }
					break;
				case "NetStream.Play.Stop" :
					stream.pause();
					stream.seek(0);
					this.dispatchEvent(new Event("end of video"));
					break;
			}
		}
		
		public function connectStream():void
		{
			stream = new NetStream(connection);
			stream.addEventListener(NetStatusEvent.NET_STATUS,onNetStatus);
			stream.addEventListener(AsyncErrorEvent.ASYNC_ERROR, asyncErrorHandler);

			var client:Object	= new Object;
			client.onMetaData	= onMetaData;
			stream.client		= client;
			
			video = new Video;
			video.attachNetStream(stream);

			addChild(video);
			stream.play(flvUrl);
		}
		
		private function onMetaData(data:Object):void
		{
			duration = data.duration;
			this.dispatchEvent(new Event("videoLoaded"));
		}	
		
		public function playFlv():void
		{
			stream.play(flvUrl);
			//stream.resume();
			playing = true;
		}

		public function togglePlay():void
		{
			stream.togglePause();
			playing =! playing;
		}		
		
		public function pause():void
		{
			stream.pause();
			playing = false;
		}
		public function resume():void
		{
			stream.resume();
			playing = true;
		}
		public function seek(time:Number):void
		{
			stream.seek(time);
			playing = false;
		}
		public function getTime():Number
		{
			return stream.time;
		}
		public function getDuration():Number
		{
			return duration;
		}
		public function isPlaying():Boolean
		{
			return playing;
		}
		private function asyncErrorHandler(event:AsyncErrorEvent):void {
            // ignore AsyncErrorEvent events.
        }
	}
}