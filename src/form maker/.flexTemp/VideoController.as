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
	import flash.net.LocalConnection;

	public class VideoController extends Sprite
	{
		private var videoPlayer	:FlvPlayer;
		private var main		:Main;
		
		private var isSlowMotion	:Boolean;
		private var myFrameR		:Number;
		private var timer			:Timer;		// ?
		public var selectedLink		:int;
		private var isPreviewed		:Boolean;	// shows whether a link is being previewed
		private var slowTimer		:Timer;		// used to control the slow motion
		private var pp				:MovieClip;	// playpause
		private var densityBar		:MovieClip;
		private var movieUrl		:String;
		private var isTextVisible	:Boolean;		
		private var linksTimes		:Array;		// # of links times 2 
		private var signLinksArray	:Array;
		private var anySelection	:Boolean;
		

		public function VideoController(url:String,times:Array):void
		{
			movieUrl 	= url;			
			linksTimes	= times;
			this.addEventListener(Event.ADDED_TO_STAGE, added, false, 0, true);
			isPreviewed = false;
			slowTimer	= new Timer(80);//higher number means slower speed
			timer 		= new Timer(135);
			timer.addEventListener(TimerEvent.TIMER, onTimer, false, 0, true);
		}

		private function added(event:Event):void{
			main = this.root  as  Main;
			pp = main.controlBar.getChildByName("pp") as MovieClip;
			densityBar = main.controlBar.getChildByName("density") as MovieClip;
						
			videoPlayer = new FlvPlayer(movieUrl, true);
			//main.debug("end of video");
			videoPlayer.addEventListener("videoLoaded", videoLoaded, false, 0, true);			
			videoPlayer.x = 3;
			videoPlayer.y = 3;
			this.addChild(videoPlayer);
			main.debug("added");
		}

		private function videoLoaded(event:Event):void{			
			videoPlayer.addEventListener("end of video", endOfVideo, false, 0, true);		
			
			isSlowMotion = false;
			
			/* test if TextField should be on or off*/
			trace("length " + main.field.length);
			if (main.field.htmlText.length > 0){
				isTextVisible = true;
				main.textButt.getChildByName("textDeselected").visible = false;
			}else{
				isTextVisible = false;
			}
			
			if (pp.ply.visible == false)
			{
				pp.ply.visible = !pp.ply.visible;
				pp.pas.visible = !pp.pas.visible;
			}else{
				pp.ply.visible = false;
				pp.pas.visible = true;
			}
			
			if(hasEventListener("videoLoaded"))
			{ main.debug(hasEventListener("MouseEvent.MOUSE_DOWN").toString());return; //do nothing
			}else{
				main.debug(hasEventListener("MouseEvent.MOUSE_DOWN").toString())
													  
				/* listeners for the buttons on the left & shift */
				pp.addEventListener(MouseEvent.CLICK,playPause, false, 0, true);
				densityBar.slider.track.addEventListener(MouseEvent.MOUSE_DOWN,sliderListen, false, 0, true);
				main.slowButt.addEventListener(MouseEvent.CLICK,slow, false, 0, true);			
				main.prevButt.addEventListener(MouseEvent.CLICK, prevLink, false, 0, true);
				main.nextButt.addEventListener(MouseEvent.CLICK, nextLink, false, 0, true);
				main.aslpahButt.addEventListener(MouseEvent.CLICK, aslpahLink, false, 0, true);
				main.textButt.addEventListener(MouseEvent.CLICK, textOnOff, false, 0, true)
				main.leftArrow.addEventListener(MouseEvent.CLICK, shiftLeft, false, 0, true);
				main.rightArrow.addEventListener(MouseEvent.CLICK, shiftRight, false, 0, true);
			
			}
			
			// draws links on the density bar
			for (var i:int=0; i < linksTimes.length; i+= 2){				
				main.drawLinkInterval(this.getXForValue(linksTimes[i]),this.getXForValue(linksTimes[i + 1] - linksTimes[i]));
			}

			signLinksArray = main.getSignLinksArray();
			// dummy timer???
			//timer = new Timer(135);
			
			timer.start(); main.debug("timer.start");
		}
		
		private function onTimer(event:TimerEvent):void
		{
			//event.updateAfterEvent();
			main.debug("currentCount: " + timer.currentCount);
			//main.debug("delay: " + timer.delay.toString()) 
			this.setThumbPositionFromValue(videoPlayer.getTime());
			this.refreshSelectedLinks(videoPlayer.getTime());
			//main.debug("currentCount: " + timer.currentCount);
			
			if (isPreviewed)
			{
				if (selectedLink == -1)
				{
					isPreviewed = false;
					this.pauseMovie();
				}
			}
		}		
		
		private function endOfVideo(event:Event):void
		{
			//pp.pas.visible = false;
			//pp.ply.visible = true;
			main.debug("end of video FROM ENDoFvIDEO");
			//event.updateAfterEvent();
			//main.debug(pp.ply.visible.toString());			
			//setThumbPositionFromValue(videoPlayer.getTime());
			slowTimer.stop();
			timer.reset();
			//timer.removeEventListener(TimerEvent.TIMER,onTimer);			
			//delete(this.timer);
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
		
		private function aslpahLink(event:MouseEvent):void
		{
			trace("aslpah link")
			navigateToURL(new URLRequest('http://www2.aslpah.ca'),'_blank')
		}
		
		public function playSignLink(time:Number):void
		{
			isPreviewed = true;
			if (videoPlayer.isPlaying())
			{
				videoPlayer.seek(time);
			}else{
				videoPlayer.seek(time);
				this.resumeMovie()
			}
			//this.resumeMovie();
			//this.setThumbPositionFromValue(time);
			//refreshSelectedLinks(time);
		}

		public function resumeMovie():void
		{
			videoPlayer.resume();
			pp.ply.visible = false;
			pp.pas.visible = true;
			timer.stop();timer.start();
/*
			if (isSlowMotion)
			{
				slowTimer.start();
			}
*/			
		}
		
		public function pauseMovie():void
		{
			videoPlayer.pause();
			pp.ply.visible = true;
			pp.pas.visible = false;
			//timer.pause();
			//timer.reset();
/*			
			if (isSlowMotion)
			{
				slowTimer.stop();
			}
*/			
		}
		
		public function playPause(event:Event = null):void
		{
			if (videoPlayer.isPlaying() && !isSlowMotion)
			{
				main.debug("1 -> pausing");
				this.pauseMovie();
				
			} 
				else if (!videoPlayer.isPlaying() && !isSlowMotion)
				{
					main.debug("1 -> resuming");
					this.resumeMovie();
				}
				
			if (isSlowMotion && slowTimer.running)
			{
				main.debug("2 -> pausing");
				slowTimer.stop();
				this.pauseMovie();
				
			}
				else if (isSlowMotion && !slowTimer.running)
				{
					main.debug("2 -> resuming");
					this.resumeMovie();
					slowTimer.start();
				}
		}

		/* Refreshes the links whether they are selected or not for the specified time */
		private function refreshSelectedLinks(time:Number):void
		{						
			anySelection = false;
			if (anySelection == false)
			{
				main.drawLinkInterval(this.getXForValue(linksTimes[selectedLink]),this.getXForValue(linksTimes[selectedLink + 1] - linksTimes[selectedLink]));
				if(selectedLink != -1){
					signLinksArray[Math.ceil(selectedLink/2)].changeToBlue();
				}
				selectedLink = -1;
				this.showHideLinkIndicator(false);
			}
			
			// work on this for loop - make a dynamic array and check the links from there
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
						main.shiftToMiddle(selectedLink/2);	// THIS SHOULD NOT BE ON THE TIMELINE
					}
					selectedLink = i;
					main.shiftToMiddle(selectedLink/2);		// THIS SHOULD NOT BE ON THE TIMELINE
					signLinksArray[Math.ceil(selectedLink/2)].changeToRed();
					break;
				}
				
				signLinksArray[Math.ceil(selectedLink/2)].changeToBlue();
			} // for loop
			//delete(anySelection);
/*			
			// supposedly forces garbage collector to clear memory, but the problem is CPU cycles
			try {
			   new LocalConnection().connect('foo');
			   new LocalConnection().connect('foo');
			} catch (e:*) {}
*/			
		}
		
		public function sliderListen(event:MouseEvent):void
		{
			event.updateAfterEvent();
			//isPreviewed = false;
			var time:Number = event.localX * sliderScale();
			
			videoPlayer.seek(time);
			
			this.setThumbPositionFromValue(time);
			refreshSelectedLinks(time);
		}

		private function sliderScale():Number
		{
			return videoPlayer.getDuration() / densityBar.slider.track.width;
		}

		private function setThumbPositionFromValue(value:Number):void
		{
			densityBar.thumb.x = value / videoPlayer.getDuration() * densityBar.slider.track.width;
			//return (videoPlayer.getTime()/videoPlayer.getDuration())*(main.controlBar.densityBar.normalSlider.trackRect.width);
		}

		private function getXForValue(value:Number):Number
		{
			return value / videoPlayer.getDuration() * densityBar.slider.track.width;
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

		/* Slow motion function. We need to take it from stage. to .movieClip  */
		public function slow(event:MouseEvent):void
		{
			isSlowMotion =! isSlowMotion;
			//main.debug("video playing: " + videoPlayer.isPlaying().toString());
			if (isSlowMotion)
			{
				main.slowButt.getChildByName("slowOff").visible = false;
				slowTimer.addEventListener(TimerEvent.TIMER,onSlowMotion, false, 0, true);
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
					this.resumeMovie();
				}
			}
			trace("--- Slomo was clicked and is now " + isSlowMotion + " at frame rate " + this.stage.frameRate);
		}// slow function

		public function textOnOff(event:MouseEvent):void
		{
			trace("Text On Off");
			isTextVisible = !isTextVisible;
			if (isTextVisible)
			{
				main.textButt.getChildByName("textDeselected").visible = false;
				main.field.visible = true;
			}
			else
			{
				main.textButt.getChildByName("textDeselected").visible = true;
				main.field.visible = false;
			}
			main.toggleText(isTextVisible);
		}
		
		private function onSlowMotion(event:TimerEvent):void
		{
			videoPlayer.seek(videoPlayer.getTime() + 0.05);
			videoPlayer.pause();	//this goes on & off for slowing down
			//event.updateAfterEvent();
		}
	}
}