#cat E0.csv | cut -d, -f1-24

cat csvjson.json | \
sed -e s/\"Div\"/\"division\"/ \
  -e s/\"Date\"/\"gameDate\"/ \
  -e s/\"Time\"/\"gameTime\"/ \
  -e s/\"HomeTeam\"/\"homeTeam\"/ \
  -e s/\"AwayTeam\"/\"awayTeam\"/ \
  -e s/\"FTHG\"/\"fullTimeHomeGoals\"/ \
  -e s/\"FTAG\"/\"fullTimeAwayGoals\"/ \
  -e s/\"FTR\"/\"fullTimeResult\"/ \
  -e s/\"HTHG\"/\"halfTimeHomeGoals\"/ \
  -e s/\"HTAG\"/\"halfTimeAwayGoals\"/ \
  -e s/\"HTR\"/\"halfTimeResult\"/ \
  -e s/\"Referee\"/\"referee\"/ \
  -e s/\"HS\"/\"homeTeamShots\"/ \
  -e s/\"AS\"/\"awayTeamShots\"/ \
  -e s/\"HST\"/\"homeTeamShotsOnTarget\"/ \
  -e s/\"AST\"/\"awayTeamShotsOnTarget\"/ \
  -e s/\"HF\"/\"homeTeamFouls\"/ \
  -e s/\"AF\"/\"awayTeamFouls\"/ \
  -e s/\"HC\"/\"homeTeamCorners\"/ \
  -e s/\"AC\"/\"awayTeamCorners\"/ \
  -e s/\"HY\"/\"homeTeamYellowCards\"/ \
  -e s/\"AY\"/\"awayTeamYellowCards\"/ \
  -e s/\"HR\"/\"homeTeamRedCards\"/ \
  -e s/\"AR\"/\"awayTeamRedCards\"/