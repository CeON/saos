-- not possible to create md5 function index in jpa
-- not possible something like: @Table(uniqueConstraints={@UniqueConstraint(name="judgment_rawText_journalEntry_unique", columnNames={"fk_judgment", "md5(rawText)", "fk_law_journal_entry"})}) 

create unique index judgment_md5_rawtext_journalentry_unique on judgment_referenced_regulation (fk_judgment, md5(raw_text), fk_law_journal_entry);
