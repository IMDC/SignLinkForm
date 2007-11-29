package{
	import flash.display.Sprite;
	import flash.display.Bitmap;	
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	import flash.events.Event;
	import flash.text.TextField;
	
	public class SignLink extends Sprite
	{
		private var main		:Main;		// an instance of the Main class
		private var startTime	:Number;
		private var endTime		:Number;
		private var slFrame		:Sprite;	// sign link frame
		private	var thumbNail   :Bitmap;	// actual sign link
		public var blueLink		:Sprite;	// blue	\
		public var redLink		:Sprite;	// red	- links under the thumbnail
		public var bothLinks	:Sprite;	// both	/
		private var urlLink		:String;	
		private var myLabel		:String;		
		private var labelTField	:TextField = new TextField();
		private var selectedLink:int;
		public var linkID		:int;
		
		public function SignLink(image:Bitmap, sTime:Number, eTime:Number, link:String, linkLabel:String=""):void
		{
			thumbNail = image; startTime = sTime; endTime = eTime; urlLink = link; myLabel = linkLabel;
			this.addEventListener(Event.ADDED_TO_STAGE, init);
		}
			
		private function init(e:Event):void
		{
			main 		= this.root as Main;	
			bothLinks	= new main.ThumbNailLink();
			redLink 	= bothLinks.getChildByName("selected") as Sprite;
			blueLink 	= bothLinks.getChildByName("deselected") as Sprite;
			addThumbnail();
			addLinks();
		}		
		
		//	Sets up the thumbnail - signlinks area
		private function addThumbnail():void
		{ 
			//background frame
			slFrame = new Sprite();
			slFrame.graphics.beginFill(0x000000);
			slFrame.graphics.drawRect(0,0,96,74);
			this.addChild(slFrame);
			
			//thumbnail w:90, h:70
			thumbNail.x 			= 3;
			thumbNail.y 			= 2;
			thumbNail.smoothing		= true;
			thumbNail.visible 		= true;
			slFrame.addChild(thumbNail);
			slFrame.buttonMode 		= true;
			slFrame.useHandCursor 	= true;
			slFrame.addEventListener(MouseEvent.CLICK, onThumbClick);
			
			//label: add TextFormat???
			labelTField.text 		= myLabel;	
			labelTField.x 			= 0;
			labelTField.y 			= 95;
			labelTField.width 		= 96;
			labelTField.height 		= 20;
			labelTField.multiline 	= true;
			labelTField.border 		= true;
			this.addChild(labelTField)
		}
		
		// bottom link
		private function addLinks():void
		{
			bothLinks.x 			= 0;
			bothLinks.y 			= 74;
			bothLinks.buttonMode 	= true;
			bothLinks.addEventListener(MouseEvent.CLICK, onLinkClick);
			this.addChild(bothLinks);
		}
		
		// change the signlink's link to red
		public function changeToRed():void
		{
			slFrame.graphics.beginFill(0xFF0000);
			slFrame.graphics.drawRect(0,0,96,74);
			slFrame.graphics.endFill();
			blueLink.visible = false;
			redLink.visible  = true;
		}
		
		// change the signlink's link to blue
		public function changeToBlue():void
		{
			slFrame.graphics.beginFill(0x000000);
			slFrame.graphics.drawRect(0,0,96,74);
			slFrame.graphics.endFill();
			redLink.visible  = false;
			blueLink.visible = true;
		}		

		// when click on a signlink turn red, play link, pause at the end
		private function onThumbClick(event:MouseEvent):void
		{
			event.updateAfterEvent();
			slFrame.graphics.beginFill(0xFF0000);
			slFrame.graphics.drawRect(0,0,96,74);
			slFrame.graphics.endFill();
			blueLink.visible 	= false;
			redLink.visible 	= true;
			this.main.getVController().playSignLink(startTime);
			main.debug(linkID.toString());
			this.main.shiftToMiddle(linkID);
		}
		
		//linkID set in main in the for loop
		public function setSLinkID(id:int):void
		{
			linkID = id;
		}
		
		// when click on a signlink's link open browser at link
		private function onLinkClick(event:MouseEvent):void
		{
			var link:URLRequest	= new URLRequest(urlLink);
			//trace("navigating to... "+urlLink);
			navigateToURL(link);
		}
	}
}