ALTER TABLE road_name_entity ADD FULLTEXT INDEX ft_kor_full_text (kor_full_text) WITH PARSER ngram;
ALTER TABLE road_name_entity ADD FULLTEXT INDEX ft_kor_initial_full_text (kor_initial_full_text) WITH PARSER ngram;
