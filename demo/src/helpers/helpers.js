export function hello(){
	return 'hello';
}

export function GetImage(content){
    var myRegexp = new RegExp(/<img.*?src="(.*?)"/);
    var match = myRegexp.exec(content);
        if (match){
            return match[1];
         }
}

export function ContentSnippet(content){
     return content.split(/\s+/).slice(0, 30).join(" ")+"...";
}