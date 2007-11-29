package 
{
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.utils.Timer;
	import flash.events.TimerEvent;
	import flash.display.Bitmap;
	import flash.display.MovieClip;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;

	public class VideoController extends Sprite
	{
		private var videoPlayer	:FlvPlayer;
		private var main		:Main;
		private var slowMotion	:Boolean;
		private var myFrameR	:Number;
		private var timer		:Timer;		// ?
		public var selectedLink	:int;
		private var isPreviewed	:Boolean;	// shows whether a link is being previewed
		private var slowTimer	:Timer;		// used to control the slow motion
		private var pp			:MovieClip;	// playpause
		private var densityBar	:MovieClip;
		private var movieUrl	:String;
		private var slowButton	:Sprite;
		private var textVisible	:Boolean;		
		private var linksTimes	:Array;		// # of links times 2 
		private var signLinksArray:Array;

		public function VideoController(url:String,times:Array):void
		{
			movieUrl 	= url;			
			linksTimes	= times;
			this.addEventListener(Event.ADDED_TO_STAGE,added);
			isPreviewed = false;
			slowTimer	= new Timer(100);//higher number means slower speed
		}

		private function added(event:Event):void{
			trace("added to stage");
			videoPlayer = new FlvPlayer(movieUrl, true);
			videoPlayer.addEventListener("videoLoaded", videoLoaded);
			this.addChild(videoPlayer);
		}

		private function videoLoaded(event:Event):void{
			main = this.root  as  Main;
			videoPlayer.addEventListener("end of video", endOfVideo);
			videoPlayer.x = 3;
			videoPlayer.y = 3;
			slowMotion = false;
			
			/* test if TextField should be on or off*/
			trace("length " + main.field.length);
			if (main.field.htmlText.length>0){
				textVisible = true;
				main.textButt.getChildByName("textDeselected").visible = false;
			}else{
				textVisible = false;
			}
			
			/* getting playPause & densityBar controls (from main) and adding listeners */
			pp = main.controlBar.getChildByName("pp") as MovieClip;
			densityBar = main.controlBar.getChildByName("density") as MovieClip;
			pp.ply.visible=false;
			pp.addEventListener(MouseEvent.CLICK,playPause);
			densityBar.slider.track.addEventListener(MouseEvent.MOUSE_DOWN,sliderListen);
			
			/* adding listeners for the buttons on the left (slowButt, nextButt, etc) */
			main.slowButt.addEventListener(MouseEvent.CLICK,slow);			
			main.prevButt.addEventListener(MouseEvent.CLICK, prevLink);
			main.nextButt.addEventListener(MouseEvent.CLICK, nextLink);
			main.aslpahButt.addEventListener(MouseEvent.CLICK, aslpahLink);
			main.textButt.addEventListener(MouseEvent.CLICK, textOnOff)
			
			/* listeners for the shift arrows */
			main.leftArrow.addEventListener(MouseEvent.CLICK, shiftLeft);
			main.rightArrow.addEventListener(MouseEvent.CLICK, shiftRight);
			
			// draws links on the density bar
			for (var i:int=0; i < linksTimes.length; i+= 2)
			{
				trace("a[i]:" + linksTimes[i] + " a[i+1]:" + linksTimes[i + 1]);
				trace("xcoordStart: " + this.getXForValue(linksTimes[i]) + "xcoorEnd: " + this.getXForValue(2 * linksTimes[i + 1] - linksTimes[i]));
				main.drawLinkInterval(this.getXForValue(linksTimes[i]),this.getXForValue(linksTimes[i + 1] - linksTimes[i]));
			}

			// dummy timer???
			timer = new Timer(50);
			timer.addEventListener(TimerEvent.TIMER,onTimer);
			timer.start();
			videoPlayer.getDuration();
		}
		
		public function onTimer(event:TimerEvent):void
		{
			event.updateAfterEvent();
			this.setThumbPositionFromValue(videoPlayer.getTime());
			this.refreshSelectedLinks(videoPlayer.getTime());
			if (isPreviewed)
			{
				if (selectedLink == -1)
				{
					trace("preview stopped");
					isPreviewed=false;
					this.pause();
				}
			}
		}		
		
		public function shiftLeft(event:MouseEvent):void
		{
			main.shiftLeft();
			trace("shiftLeft");
		}
		
		public function shiftRight(event:MouseEvent):void
		{
			main.shiftRight();
			trace("shiftRight");			
		}
		
		public function nextLink(event:MouseEvent):void
		{
			trace("next link");
			isPreviewed = false;
			if (linksTimes.length==0)
			{
				return;
			}
			var end:Boolean = true;
			for (var i:int=0; i <linksTimes.length; i+=2)
			{
				if (videoPlayer.getTime() < linksTimes[i])
				{
					videoPlayer.seek(linksTimes[i]);
					main.shiftToMiddle(i/2);
					end = false;
					break;
				}  
			}
			if (end)
			{
				main.shiftToStart();
				videoPlayer.seek(linksTimes[0]);
			}
		}
		
		public function prevLink(event:MouseEvent):void
		{
			trace("previous link");
			isPreviewed = false;
			if (linksTimes.length==0)
			{
				return;
			}
			var beginning:Boolean = true;
			for (var i:int=linksTimes.length-1; i >=0; i-=2)
			{
				if (videoPlayer.getTime() > linksTimes[i])
				{
					videoPlayer.seek(linksTimes[i-1]);
					main.shiftToMiddle(i/2);
					beginning = false;
					break;
				}  
			}
			if (beginning)
			{
				main.shiftToEnd();
				videoPlayer.seek(linksTimes[linksTimes.length-2]);
			}
		}
		
		private function toggleText(event:MouseEvent):void
		{
			trace ("text button clicked");
			main.field.visible = !main.field.visible;
		}
		
		private function aslpahLink(event:MouseEvent):void
		{
			trace("aslpah link")
			navigateToURL(new URLRequest('http://www2.aslpah.ca'),'_blank')
		}
		
		public function playSignLink(time:Number):void
		{
			isPreviewed=true;
			trace("--- From XML: VideoLink starts at: " + time);
			var i:int=0;
			videoPlayer.seek(time);
			this.resume();
		}

		public function resume():void
		{
			videoPlayer.resume();
			pp.ply.visible=false;
			pp.pas.visible=true;
			trace("resuming");
			//timer.start();
			if (slowMotion)
			{
				slowTimer.start();
			}
		}
		
		public function pause():void
		{
			videoPlayer.pause();
			pp.ply.visible=true;
			pp.pas.visible=false;
			trace("pausing");
			//timer.stop();
			if (slowMotion)
			{
				slowTimer.stop();
			}
		}

		/* Refreshes the links whether they are selected or not for the specified time */
		private function refreshSelectedLinks(time:Number):void
		{
			signLinksArray = main.getSignLinksArray();
			var anySelection:Boolean=false;
			for (var i:int=0; i < linksTimes.length; i+= 2)
			{
				if (time >= linksTimes[i] && time <= linksTimes[i + 1])
				{
					anySelection = true;
					if (selectedLink != i)
					{
						main.drawLinkInterval(this.getXForValue(linksTimes[selectedLink]),this.getXForValue(linksTimes[selectedLink + 1] - linksTimes[selectedLink]));
						main.drawLinkInterval(this.getXForValue(linksTimes[i]),this.getXForValue(linksTimes[i + 1] - linksTimes[i]),true);
						this.showHideLinkIndicator(true);
						signLinksArray[Math.ceil(selectedLink/2)].changeToBlue();
						main.shiftToMiddle(selectedLink/2);
						//main.shiftToMiddle(Math.ceil(selectedLink/2));
					}
					//main.shiftToMiddle(i);
					selectedLink = i;
					main.shiftToMiddle(selectedLink/2);
					signLinksArray[Math.ceil(selectedLink/2)].changeToRed();
					break;
				}
				signLinksArray[Math.ceil(selectedLink/2)].changeToBlue();
			}
			if (! anySelection)
			{
				main.drawLinkInterval(this.getXForValue(linksTimes[selectedLink]),this.getXForValue(linksTimes[selectedLink + 1] - linksTimes[selectedLink]));
				if(selectedLink != -1){
					signLinksArray[Math.ceil(selectedLink/2)].changeToBlue();
				}
				selectedLink=-1;
				this.showHideLinkIndicator(false);
			}
		}
		
		public function sliderListen(event:MouseEvent):void
		{
			event.updateAfterEvent();
			isPreviewed=false;
			var time:Number=event.localX * sliderScale();
			trace("mouseX: " + event.localX);
			trace("time: " + time);
			videoPlayer.seek(time);
			trace("actual time: " + videoPlayer.getTime());
			this.setThumbPositionFromValue(time);
			refreshSelectedLinks(time);
		}

		private function sliderScale():Number
		{
			return videoPlayer.getDuration() / densityBar.slider.track.width;
		}

		private function setThumbPositionFromValue(value:Number):void
		{
			densityBar.thumb.x=value / videoPlayer.getDuration() * densityBar.slider.track.width;
			//return (videoPlayer.getTime()/videoPlayer.getDuration())*(main.controlBar.densityBar.normalSlider.trackRect.width);
		}

		private function getXForValue(value:Number):Number
		{
			return value / videoPlayer.getDuration() * densityBar.slider.track.width;
		}

		public function endOfVideo(event:Event):void
		{
			trace("end of video");
			//timer.stop();

			//video.stream.seek(0);
			this.pause();
			setThumbPositionFromValue(videoPlayer.getTime());
		}

		public function playPause(event:Event=null):void
		{
			pp.ply.visible =! pp.ply.visible;
			pp.pas.visible =! pp.pas.visible;
			//videoPlayer.togglePlay();
			//trace("timer Running: "+timer.running);
			if (videoPlayer.isPlaying())
			{
				trace("pausing");
				videoPlayer.pause();
				timer.stop();
			}
			else
			{
				trace("resuming");
				videoPlayer.resume();
				timer.start();
			}
			if (slowMotion && slowTimer.running)
			{
				slowTimer.stop();
				videoPlayer.pause();
			}
			else if (slowMotion && ! slowTimer.running)
			{
				videoPlayer.resume();
				slowTimer.start();
			}
		}

		/* Shows - Hides the linkIndicator */
		public function showHideLinkIndicator(show:Boolean):void
		{
			if (show){
				main.linkIndicator.visible=true;
			}
			else{
				main.linkIndicator.visible=false;
			}
		}

/*   Slow motion function. We need to take it from stage. to .movieClip  */
		//George
		public function slow(event:MouseEvent):void
		{
			slowMotion=! slowMotion;
			trace("video playing: "+videoPlayer.isPlaying());
			if (slowMotion)
			{
				main.slowButt.getChildByName("slowOff").visible = false;
				slowTimer.addEventListener(TimerEvent.TIMER,onSlowMotion);
				if (videoPlayer.isPlaying())
				{
					slowTimer.start();
				}
			}
			else
			{
				slowTimer.stop();
				slowTimer.removeEventListener(TimerEvent.TIMER,onSlowMotion);
				main.slowButt.getChildByName("slowOff").visible =  true;
				//trace("videoPlayer playing: "+videoPlayer.isPlaying());
				if (pp.pas.visible) //if supposed to be playing
				{
					videoPlayer.resume();
				}
			}
			trace("--- Slomo was clicked and is now " + slowMotion + " at frame rate " + this.stage.frameRate);
		}// slow function

		public function textOnOff(event:MouseEvent):void
		{
			trace("Text On Off");
			textVisible = !textVisible;
			if (textVisible)
			{
				main.textButt.getChildByName("textDeselected").visible = false;
				main.field.visible = true;
			}
			else
			{
				main.textButt.getChildByName("textDeselected").visible = true;
				main.field.visible = false;
			}
		}
		
		private function onSlowMotion(event:TimerEvent):void{
			videoPlayer.seek(videoPlayer.getTime() + 0.05);
			videoPlayer.pause();
			event.updateAfterEvent();
		}
	}
}