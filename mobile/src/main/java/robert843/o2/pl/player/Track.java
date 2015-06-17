package robert843.o2.pl.player;

public class Track {
	private String id;
	private String audioUrl;
	private String title;
	
	public Track(String id, String title, String audioUrl){
		setId(id);
		setAudioUrl(audioUrl);
		setTitle(title);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAudioUrl() {
		return audioUrl;
	}
	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
