package jpa.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Line extends BaseEntity{

	private String color;

	@Column(unique = true)
	private String name;

	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
	private List<LineStation> lineStations = new ArrayList<>();

	protected Line() {
	}

	public Line(String name) {
		this.name = name;
	}

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public List<Station> getStations() {
		return lineStations.stream()
			.map(LineStation::getStation)
			.collect(Collectors.toList());
	}

	public void changeName(String name) {
		this.name = name;
	}

	public void addStation(Station station, int distance) {
		if (isExistStation(station)) {
			throw new IllegalArgumentException(String.format("%s 역은 %s 노선에 이미 포함된 역입니다.", station.getName(), this.name));
		}
		this.lineStations.add(new LineStation(this, station, distance, lastStation()));
	}

	public int distanceFromPreviousStation(Station station) {
		return this.lineStations.stream()
			.filter(lineStation -> lineStation.getStation().equals(station))
			.map(LineStation::getDistance)
			.findFirst()
			.orElseThrow(() ->
				new IllegalArgumentException(String.format("%s 역은 %s 노선에 포함되지 않는 역입니다.", station.getName(), this.name))
			);
	}

	public Station previousStation(Station station) {
		return this.lineStations.stream()
			.filter(lineStation -> lineStation.getStation().equals(station))
			.findFirst()
			.orElseThrow(() ->
				new IllegalArgumentException(String.format("%s 역은 %s 노선에 포함되지 않는 역입니다.", station.getName(), this.name))
			).getPreStation();
	}

	public Station lastStation() {
		if (this.lineStations.isEmpty()) {
			return null;
		}
		return this.lineStations.get(this.lineStations.size() - 1).getStation();
	}

	private boolean isExistStation(Station station) {
		return this.lineStations.stream()
			.anyMatch(lineStation -> lineStation.getStation().equals(station));
	}
}
